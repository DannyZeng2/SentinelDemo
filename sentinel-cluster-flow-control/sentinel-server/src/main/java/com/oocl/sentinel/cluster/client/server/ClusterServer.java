package com.oocl.sentinel.cluster.client.server;

import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterParamFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.server.ClusterTokenServer;
import com.alibaba.csp.sentinel.cluster.server.SentinelDefaultTokenServer;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ClusterServer {

    private static final String APP_NAME = "SentinelCluster";

    private static final String FLOW_POSTFIX = "-flow-rules";

    private static final int CLUSTER_SERVER_PORT = 12345;


    private static final String REMOTE_ADDRESS = "localhost";
    private static final String GROUP_ID = "SENTINEL_GROUP";

    /**
     * 初始化集群限流的Supplier
     * 这样如果后期集群限流的规则发生变更的话，系统可以自动感知到
     */
    private void initClusterFlowSupplier() {
        // 为集群流控注册一个Supplier，该Supplier会根据namespace动态创建数据源
        ClusterFlowRuleManager.setPropertySupplier(namespace -> {
            // 使用 Nacos 数据源作为配置中心，需要在 REMOTE_ADDRESS 上启动一个 Nacos 的服务
            ReadableDataSource<String, List<FlowRule>> ds = new NacosDataSource<>(REMOTE_ADDRESS, GROUP_ID,
                    namespace + FLOW_POSTFIX,
                    source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
            return ds.getProperty();
        });
    }

    /**
     * 加载namespace的集合以及ServerTransportConfig
     * 最好还要再为他们每个都注册一个SentinelProperty，这样的话可以动态的修改这些配置项
     */
    private void loadServerConfig(){
        // 加载namespace
        ClusterServerConfigManager.loadServerNamespaceSet(Collections.singleton(APP_NAME));
        // 加载ServerTransportConfig
        ClusterServerConfigManager.loadGlobalTransportConfig(new ServerTransportConfig()
                .setIdleSeconds(600)
                .setPort(CLUSTER_SERVER_PORT));
    }

    /**
     * 初始化工作
     */
    public void init() {
        // 初始化集群限流的规则
        initClusterFlowSupplier();
        // 加载服务端的配置
        loadServerConfig();
    }

    /**
     * 启动ClusterToken服务端
     */
    public void start() throws Exception {
        // 创建一个 ClusterTokenServer 的实例，独立模式
        ClusterTokenServer tokenServer = new SentinelDefaultTokenServer();
        // 启动
        tokenServer.start();
    }


    public static void main(String[] args) throws Exception {
        ClusterServer clusterServer = new ClusterServer();
        clusterServer.init();
        clusterServer.start();
    }

}
