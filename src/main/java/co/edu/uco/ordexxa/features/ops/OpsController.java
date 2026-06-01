package co.edu.uco.ordexxa.features.ops;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ops")
public class OpsController {

    private final VaultStatusService vaultStatusService;

    public OpsController(final VaultStatusService vaultStatusService) {
        this.vaultStatusService = vaultStatusService;
    }

    @GetMapping("/vault/status")
    public VaultStatusResponse vaultStatus() {
        return vaultStatusService.checkStatus();
    }
}
