package com.auth_server.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.auth_server.dto.req.ChangePasswordReq;
import com.auth_server.dto.req.ForgotPasswordReq;
import com.auth_server.dto.req.LoginReq;
import com.auth_server.dto.req.RegisterReq;
import com.auth_server.dto.req.ResetPasswordReq;
import com.auth_server.dto.res.PermissionListDTO;
import com.auth_server.dto.res.UserListDTO;
import com.auth_server.entity.Permission;
import com.auth_server.entity.User;
import com.auth_server.exception.AuthValidationException;
import com.auth_server.exception.ResourceNotFoundException;
import com.auth_server.security.CustomUserDetails;
import com.auth_server.security.SystemUserDetails;
import com.auth_server.service.AuthTokenService;
import com.auth_server.service.SystemUserService;
import com.auth_server.service.UserPermissionService;
import com.auth_server.service.UserService;
import com.auth_server.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

    private final SystemUserService service;
    private final AuthTokenService authTokenService;

    private final UserService userService;
    private final UserPermissionService userPermissionService;

    public final static String VERIFY_URI = "/verify-email";
    public final static String RESET_PASSWORD_URI = "/reset-password";

    @ModelAttribute("currentPath")
    public String getCurrentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/profile")
    public String getProfile(Authentication auth, Model model) {
        Long userId = Utils.getAuthId(auth);
        UserListDTO dto = userService.findDTOById(userId);
        model.addAttribute("user", dto);
        return "profile";
    }

    @GetMapping("/permissions")
    public String getPermissions(Authentication auth, Model model) {
        Long userId = Utils.getAuthId(auth);
        List<PermissionListDTO> dtos = userPermissionService.findPermissionListByUserId(userId);
        if (dtos.isEmpty()) {
            return "redirect:/";
        }
        Map<Permission, String> permissionDesc = Map.of(
                Permission.CUSTOMER_MANAGE, "Quản lý khách hàng",
                Permission.ORDER_MANAGE, "Quản lý đơn hàng",
                Permission.PRODUCT_MANAGE, "Quản lý Sản phẩm",
                Permission.REPORT_VIEW, "Quản lý doanh thu",
                Permission.USER_MANAGE, "Quản lý tài khoản");
        model.addAttribute("permissionDesc", permissionDesc);
        model.addAttribute("permissions", dtos);
        return "permissions";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute(name = "data") RegisterReq data) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute(name = "data") RegisterReq data,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            data.setPassword(null);
            data.setConfirmPassword(null);

            return "register";
        }
        service.register(data);

        return "redirect:/login?account_created";
    }

    @GetMapping("login")
    public String login(@ModelAttribute(name = "data") LoginReq data, BindingResult bindingResult,
            HttpServletRequest request) {
        Exception ex = (Exception) request.getSession()
                .getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        if (ex instanceof AuthValidationException validationEx) {
            List<FieldError> errors = validationEx.getErrors();
            errors.forEach(bindingResult::addError);
        }

        return "login";
    }

    @GetMapping(VERIFY_URI)
    public String verficationEmail(@RequestParam(required = false) String token, Model model) {
        if (token == null || token.isBlank()) {
            model.addAttribute("result", 400);
            return "verify-email";
        }

        try {
            User user = authTokenService.verifyEmail(token);
            model.addAttribute("result", 200);
            model.addAttribute("user", user);
            return "verify-email";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("result", 404);
            return "verify-email";
        } catch (Exception e) {
            model.addAttribute("result", 500);
            return "verify-email";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(
            @ModelAttribute("req") ForgotPasswordReq req,
            @ModelAttribute("email") String email) {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @Valid @ModelAttribute("req") ForgotPasswordReq req,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "forgot-password";
        }

        authTokenService.generateForgotPasswordToken(req.getEmail());

        redirectAttributes.addFlashAttribute("email", req.getEmail());

        return "redirect:/forgot-password";
    }

    @GetMapping(RESET_PASSWORD_URI)
    public String resetPassword(@ModelAttribute("data") ResetPasswordReq req,
            @RequestParam(required = false) String token, Model model) {
        if (Strings.isBlank(token)) {
            model.addAttribute("result", 400);
            return "reset-password";
        }

        req.setToken(token);

        return "reset-password";
    }

    @PostMapping(RESET_PASSWORD_URI)
    public String resetPassword(@Valid @ModelAttribute("data") ResetPasswordReq req, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            req.setPassword("");
            req.setConfirmPassword("");
            return "reset-password";
        }

        service.resetPassword(req);
        return "redirect:/login?reset-password";
    }

    @GetMapping("/change-password")
    public String changePassword(@ModelAttribute("data") ChangePasswordReq data,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        if (!(userDetails instanceof SystemUserDetails)) {
            model.addAttribute("error", "Tài khoản của bạn không thuộc quản lý của chúng tôi");
            return "change-password";
        }

        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("data") ChangePasswordReq data,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "change-password";
        }

        service.changePassword(data, userDetails.getId());

        return "redirect:/?change-password";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

}
