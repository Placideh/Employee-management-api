package com.placideh.employees.filters;

import com.placideh.employees.model.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter  extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // First Lets Type cast ServletRequest to HttpServlets response and request
        HttpServletRequest httpRequest=(HttpServletRequest)request;
        HttpServletResponse httpResponse=(HttpServletResponse)response;
//		to access any protected resource we want the client to pass the header
        String authHeader=httpRequest.getHeader("Authorization");
        if(authHeader!=null) {
            String[] authHeaderArr=authHeader.split("Bearer ");
            if(authHeaderArr.length > 1 && authHeaderArr[1]!=null) {
                String token=authHeaderArr[1];
                try {
                    //fetching for the claims which are in the payload
                    Claims claims= Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                            .parseClaimsJws(token).getBody();
                    httpRequest.setAttribute("nationalId", Integer.parseInt(claims.get("nationalId").toString()));
                }catch(Exception e) {
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(),"invalid/expired token");
                    return ;

                }
            }else {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(),"Authorization token must be Bearer [token]");
                return;
            }
        }else {
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(),"Authorization token must provided");
            return;
        }
        chain.doFilter(request, response);

    }
}
