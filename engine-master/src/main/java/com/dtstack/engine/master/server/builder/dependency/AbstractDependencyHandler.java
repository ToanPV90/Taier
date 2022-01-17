package com.dtstack.engine.master.server.builder.dependency;

import com.dtstack.engine.common.enums.DependencyType;
import com.dtstack.engine.domain.ScheduleTaskShade;
import com.dtstack.engine.master.enums.RelyRule;
import com.dtstack.engine.master.server.builder.ScheduleConf;

import java.util.List;

/**
 * @Auther: dazhi
 * @Date: 2022/1/4 4:06 PM
 * @Email: dazhi@dtstack.com
 * @Description:
 */
public abstract class AbstractDependencyHandler implements DependencyHandler {

    /**
     * 前缀
     */
    protected String keyPreStr;

    /**
     * 当前任务
     */
    protected ScheduleTaskShade currentTaskShade;

    /**
     * 上游任务
     */
    protected List<ScheduleTaskShade> taskShadeList;

    /**
     * 下一个依赖处理器
     */
    private DependencyHandler nextDependencyHandler;

    public AbstractDependencyHandler(String keyPreStr,
                                     ScheduleTaskShade currentTaskShade,
                                     List<ScheduleTaskShade> taskShadeList) {
        this.keyPreStr = keyPreStr;
        this.currentTaskShade = currentTaskShade;
        this.taskShadeList = taskShadeList;
    }

    /**
     * 获得依赖规则
     *
     * @param scheduleConf 调度信息
     * @return 依赖规则
     */
    protected Integer getRule(ScheduleConf scheduleConf) {
        Integer selfReliance = scheduleConf.getSelfReliance();

        if (DependencyType.SELF_DEPENDENCY_SUCCESS.getType().equals(selfReliance)
                ||DependencyType.PRE_PERIOD_CHILD_DEPENDENCY_SUCCESS.getType().equals(selfReliance) ) {
            return RelyRule.RUN_SUCCESS.getType();
        } else if (DependencyType.SELF_DEPENDENCY_END.getType().equals(selfReliance)
                || DependencyType.PRE_PERIOD_CHILD_DEPENDENCY_END.getType().equals(selfReliance)) {
            return RelyRule.RUN_FINISH.getType();
        }
        return RelyRule.RUN_SUCCESS.getType();
    }

    @Override
    public DependencyHandler next() {
        return nextDependencyHandler;
    }

    @Override
    public void setNext(DependencyHandler dependencyHandler) {
        this.nextDependencyHandler = dependencyHandler;
    }
}