package com.zhcs.controller.api;

import com.qiniu.api.net.Http;
import com.zhcs.context.PlatformContext;
import com.zhcs.entity.*;
import com.zhcs.service.*;
import com.zhcs.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "微信市民端爆料" , description = "爆料相关")
@Controller
@RequestMapping("/api/citizen/brknews")
@CrossOrigin
public class ApiCitizenBrknewsController {

	@Autowired
	private BrknewsService brknewsService;
	@Autowired
	private CitizenService citizenService;
	@Autowired
	private SerianoService serianoService;
	@Autowired
	private EventService eventService;
	@Autowired
	private EventLogService eventLogService;
	@Autowired
	private WorkflowService workflowService;

	@ApiOperation(value = "我要爆料" , notes = "我要爆料")
	@RequestMapping(value = "/report" , method = RequestMethod.POST)
	@ResponseBody
	public R report(@RequestBody BrknewsReportEntity brknewsReport){
		// 检查openId是否在我们市民关注列表，不在表示无效
		if (StringUtil.isValid(brknewsReport.getOpenId())) {
			CitizenEntity citizen = citizenService.queryObjectByWeChatId(brknewsReport.getOpenId());
			if (StringUtil.isValid(citizen)) {
				String caseno = serianoService.getId("event.no", DateUtil.getSystemDateStr("yyyyMM"));
				String current = PlatformContext.getGoalbalContext("bn", "bl", String.class);
				WorkflowEntity we = workflowService.queryOtherNodeObject(current, "1");
				Long userId = citizen.getId();

				BrknewsEntity brknewsEntity = new BrknewsEntity();
				brknewsEntity.setType(brknewsReport.getType());
				brknewsEntity.setLat(brknewsReport.getLat());
				brknewsEntity.setLng(brknewsReport.getLng());
				brknewsEntity.setAdds(brknewsReport.getAdds());
				brknewsEntity.setContent(brknewsReport.getContent());
				brknewsEntity.setImg(brknewsReport.getImg());
				brknewsEntity.setSource(PlatformContext.getGoalbalContext("caseSource0", String.class));
				brknewsEntity.setCaseno(caseno);
				BeanUtil.fillCCUUD(brknewsEntity, userId, userId);
				// 因为爆料和任务派单环节是在一起，也就是爆料是会自动运行并结束，所以案件爆料后的第一个环节不是爆料而是派单，所以先把爆料保存到日志表，
				// 保存日志信息
				EventLogEntity eventlog = new EventLogEntity();
				eventlog.setAddr(brknewsEntity.getAdds());
				eventlog.setBusseg(brknewsEntity.getType());
				eventlog.setLat(brknewsEntity.getLat());
				eventlog.setLng(brknewsEntity.getLng());
				eventlog.setNo(brknewsEntity.getCaseno());
				eventlog.setSour(brknewsEntity.getSource());
				eventlog.setQimg(brknewsEntity.getImg());
				eventlog.setQdescribe(brknewsEntity.getContent());
				eventlog.setCurrent(current);
				eventlog.setNext(we.getNodeno());
				eventlog.setFlag("1");
				BeanUtil.fillCCUUD(eventlog, userId, userId);
				eventLogService.save(eventlog);
				// 保存当前信息
				EventEntity event = new EventEntity();
				event.setAddr(brknewsEntity.getAdds());
				event.setBusseg(brknewsEntity.getType());
				event.setLat(brknewsEntity.getLat());
				event.setLng(brknewsEntity.getLng());
				event.setNo(brknewsEntity.getCaseno());
				event.setSour(brknewsEntity.getSource());
				event.setQimg(brknewsEntity.getImg());
				event.setQdescribe(brknewsEntity.getContent());
				event.setCurrent(we.getNodeno());
				BeanUtil.fillCCUUD(event, userId, userId);
				eventService.save(event);
				return R.ok();
			} else {
				return R.error(60000, "openId无效");
			}
		} else {
			return R.error(60000, "openId无效");
		}
	}

	@ApiOperation(value = "获取爆料列表" , notes = "获取爆料列表")
	@RequestMapping(value = "/getList" , method = RequestMethod.POST)
	@ResponseBody
	public R getList(@RequestBody CitizenPageEntity citizenPage){
		// 检查openId是否在我们市民关注列表，不在表示无效
		if (StringUtil.isValid(citizenPage.getOpenId())) {
			CitizenEntity citizen = citizenService.queryObjectByWeChatId(citizenPage.getOpenId());
			if (StringUtil.isValid(citizen)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("offset", (citizenPage.getPage() - 1) * citizenPage.getPageSize());
				map.put("limit", citizenPage.getPageSize());
				//查询列表数据
				List<Map<String, Object>> brknewsList = brknewsService.queryList2(map);

				int total = brknewsService.queryTotal(map);
				PageUtils pageUtil = new PageUtils(brknewsList, total, citizenPage.getPageSize(), citizenPage.getPage());
				return R.ok().putData(pageUtil);
			} else {
				return R.error(60000, "openId无效");
			}
		} else {
			return R.error(60000, "openId无效");
		}
	}

}
