package poc.custom.token.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
public class PermissionsService {

    public boolean hasPermission(List<String> userPermissions, String requiredPermission) {
        return userPermissions.contains(requiredPermission);
    }
}
