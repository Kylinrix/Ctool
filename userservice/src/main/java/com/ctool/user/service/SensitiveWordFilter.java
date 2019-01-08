package com.ctool.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.ctool.remoteService.SensitiveWordService;
import org.springframework.web.util.HtmlUtils;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/7 21:33
 * @Email: Kylinrix@outlook.com
 * @Description:
 */

@Service
public class SensitiveWordFilter implements SensitiveWordService {
    @Override
    public String WordFilter(String content) {
        return HtmlUtils.htmlEscape(content);
    }
}
