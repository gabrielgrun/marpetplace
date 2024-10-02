package marpetplace.api.dto.response;

import marpetplace.api.domain.entity.Admin;

import java.util.UUID;

public record AdminDetailedResponse(UUID id, String login) {

    public AdminDetailedResponse(Admin admin){
        this(admin.getId(), admin.getLogin());
    }
}
