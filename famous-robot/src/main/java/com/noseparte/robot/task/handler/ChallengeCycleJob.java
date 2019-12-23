package com.noseparte.robot.task.handler;

import com.noseparte.common.bean.ChapterBean;
import com.noseparte.common.bean.SchoolBean;
import com.noseparte.common.bean.StateCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.resources.ChapterConf;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.chapter.cmd.ChallengeCmd;
import com.noseparte.robot.chapter.request.ChallengeRequest;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/15 15:01
 * @Description:
 *
 *          <p>robot 连续不间断地</p>
 *          <p>挑战关卡任务</p>
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ChallengeCycleJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Trigger trigger = context.getTrigger();
        JobDataMap jobDataMap = trigger.getJobDataMap();

        long robotId = jobDataMap.getLong("robotId");
        RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
        Map<Long, Robot> robotMap = robotMgr.getRobotMap();
        Robot robot = robotMap.get(robotId);
        Map<Integer, ChapterConf> chapterConfMap = ConfigManager.chapterConfMap;
        List<ChapterConf> chapterConfs = chapterConfMap.values().stream()
                .sorted(Comparator.comparing(ChapterConf::getId))
                .collect(Collectors.toList());
        Map<Integer, ChapterBean> chapters = robot.getChapter().getChapters();
        if (Collections.EMPTY_LIST == chapters) {
            if(log.isDebugEnabled()){
                log.debug("robot-- {} 还没有打过关卡", robot.getRid());
            }
        }
        Map<Integer, SchoolBean> schools = robot.getOccupation().getSchools();
        int schoolId = schools.values().stream() // robot职业 默认战士
                .filter(school -> String.valueOf(school.getSchoolId()).startsWith("1"))
                .findFirst().get().getSchoolId();
        for (ChapterConf config : chapterConfs) {
            Integer chapterId = config.getId();
            if (chapters.containsKey(chapterId) && chapters.get(chapterId).getState() == StateCode.COMPLETED) { // 打过的关卡跳过
                continue;
            }
            ChallengeCmd challengeCmd = new ChallengeCmd();
            challengeCmd.setRid(robot.getRid());
            challengeCmd.setSchoolId(schoolId);
            challengeCmd.setChapterId(chapterId);
            challengeCmd.setCmd(RegisterProtocol.CHAPTER_CHALLENGE_REQ);

            try {
                if(log.isDebugEnabled()){
                    log.debug("robot {} 使用职业 {} 在打关卡 {}", robot.getRid(), schoolId, chapterId);
                }
                new ChallengeRequest(challengeCmd).execute();
            } catch (Exception e) {
                if(log.isErrorEnabled()){
                    log.error("robot {}， 挑战关卡 {} 失败", robot.getRid(), schoolId);
                }
            }
            break;
        }
    }



}
