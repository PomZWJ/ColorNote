package com.bluefatty.aspect;

import com.bluefatty.common.ResponseParams;
import com.bluefatty.exception.MessageCode;
import com.bluefatty.service.IUserService;
import com.bluefatty.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-27
 */
@Slf4j
@Component
@Aspect
public class AspectController {
    @Autowired
    private IUserService userService;

    @Pointcut(" execution(* com.bluefatty.controller.CommonController.*(..)) " +
            "|| execution(* com.bluefatty.controller.NoteController.*(..))" +
            "|| execution(* com.bluefatty.controller.NoteKindController.*(..))"+
            "|| execution(* com.bluefatty.controller.UserController.getCurrentLoginUserInfo(..))"+
            "|| execution(* com.bluefatty.controller.UserController.getUserIndexInfo(..))")
    public void pointcut() {}


    /**
     * 环绕通知，ProceedingJoinPoint 参数必须要，并且要放行，不然方法就处于阻塞状态
     * @param pjp
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp)throws Throwable{
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        responseParams.setResultCode(MessageCode.ERROR_TOKEN_IS_NOT_EXIST.getCode());
        responseParams.setResultMsg(MessageCode.ERROR_TOKEN_IS_NOT_EXIST.getMsg());
        try{
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();
            String userId = request.getParameter("userId");
            String token = request.getParameter("token");
            boolean flag = false;
            if (StringUtils.isEmpty(userId)) {
                responseParams.setParams(flag);
            }
            if (StringUtils.isEmpty(token)) {
                responseParams.setParams(flag);
            }
            if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(token)){
                Boolean isCorrect = userService.determineUserTokenIsCorrect(userId, token);
                if(isCorrect){
                    flag=true;
                }
                responseParams.setParams(flag);
            }
            if(flag==false){
                return responseParams;
            }
        }catch (Throwable e){
           log.error("aspect异常,e={}",e);
        }
        return pjp.proceed();
    }

}
