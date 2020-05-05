package com.oocl.sentinel.flow;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    public static final String USER_RES = "userResource";

    public UserService(){
        // 定义热点限流的规则，对第一个参数设置 qps 限流模式，阈值为5
        FlowRule rule = new FlowRule();
        rule.setResource(USER_RES);
        // 限流类型，qps
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 设置阈值
        rule.setCount(1);
        // 限制哪个调用方
        rule.setLimitApp(RuleConstant.LIMIT_APP_DEFAULT);
        // 基于调用关系的流量控制
        rule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        // 流控策略
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        FlowRuleManager.loadRules(Collections.singletonList(rule));
    }

    public User getUser(Long uid){
        Entry entry = null;
        User user = new User();
        user.setUid(uid);
        user.setName("user-" + uid);

        try {
            entry = SphU.entry(USER_RES);
            user.setState("pass.....");
        }catch(BlockException e){
            System.out.println("[getUser] has been protected! Time="+ System.currentTimeMillis());
            user.setState("block.....");
        }finally {
            if(entry!=null){
                entry.exit();
            }
        }
        return user;
    }


    public static class User {
        private Long uid;
        private String name;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        private String state;

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}