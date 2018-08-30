/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursomc.com.felipebatista.cursomc.tenant;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

import static org.springframework.util.CollectionUtils.containsAny;

public class ScopeValidationContext {

    private List<String> scopes;
    private AccessScopeCheckPolicy checkPolicy;

    public ScopeValidationContext(AccessScopeCheckPolicy checkPolicy, String... scopes) {
        if (ArrayUtils.isEmpty(scopes)) {
            throw new IllegalArgumentException("É necessário informar pelo menos um scope");
        }

        this.scopes = Arrays.asList(scopes);
        this.checkPolicy = checkPolicy;
    }

    boolean hasScopes(List<String> scopes) {
        if (scopes == null || scopes.isEmpty()) {
            return false;
        }
        switch (checkPolicy) {
            case ALL:
                return scopes.containsAll(this.scopes);
            case ANY:
                return containsAny(this.scopes, scopes);
            default:
                return false;
        }
    }
}
