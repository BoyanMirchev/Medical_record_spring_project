package com.example.Medical_record_project_Final.util;

import com.example.Medical_record_project_Final.config.MedicalRecordUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class LoggedUserUtil {

    private LoggedUserUtil() {
    }

    public static MedicalRecordUserDetails getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof MedicalRecordUserDetails)) {
            return null;
        }

        return (MedicalRecordUserDetails) authentication.getPrincipal();
    }

    public static Integer getLoggedUserId() {
        MedicalRecordUserDetails userDetails = getLoggedUser();
        return userDetails != null ? userDetails.getId() : null;
    }

    public static String getLoggedUsername() {
        MedicalRecordUserDetails userDetails = getLoggedUser();
        return userDetails != null ? userDetails.getUsername() : null;
    }

    public static boolean hasRole(String role) {
        MedicalRecordUserDetails userDetails = getLoggedUser();
        return userDetails != null && role.equalsIgnoreCase(userDetails.getRoleName());
    }
}