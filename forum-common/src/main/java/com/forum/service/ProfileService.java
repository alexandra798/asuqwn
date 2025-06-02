package com.forum.service;

import com.forum.model.user.dto.MessageDTO;
import com.forum.model.user.vo.AnswerListVO;
import com.forum.model.user.vo.PremiumFeeVO;
import com.forum.model.user.vo.ProfileVO;
import com.forum.utils.PageResult;

import java.time.LocalDateTime;

public interface ProfileService {

    boolean changePassword(String oldPassword, String newPassword, Long userId);

    PageResult<ProfileVO> getProfileByUser(Long userId);

    PremiumFeeVO getPremiumFeeByUser(LocalDateTime time, Long userId);

    //应当再写一个上传缴费凭证（图片）的功能以避免缴费后管理员忘记更新账户状态的情况发生，如下

    void uploadPaymentProof(MessageDTO messageDTO);


    //在一个用户的缴费板块里，应当显示以下内容：本月（xx日到xx日）应缴的费用为xxUSD、你完成了/未完成缴费、你的a阶段缴费截止日期为～b阶段缴费截止日期为～、缴费记录（点击按钮后展开（当然这个展开功能是由前端实现的））
    //基本上，叫“getxx”类名字的methods(functions)的返回类型都为xxVO

    //会员到期不交费自动取消的逻辑是不是也要在这里写
    //consumer这种东西就是专门服务于【消息队列】的，而消息队列又是只有在想实现通知功能时才需要用到的架构/工具，所以我看的那些java系统课里很少出现consumer的影子

    PageResult<AnswerListVO> getAnswersList(Long userId);
}
