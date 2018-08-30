package cursomc.com.felipebatista.cursomc.tenant;

import java.util.Objects;

public final class UserAccessEntityHash {

    static final int BASE_64_OPTIONS = Base64.DONT_BREAK_LINES | Base64.URL_SAFE;
    static final String PASS_PATTERN = "%s:%s:BTH_7745@##wwddfggJKKllossccooppfs7755544=5___0";

    private UserAccessEntityHash() {
        throw new UnsupportedOperationException("Not supported.");
    }

    static Encoded encode(UserAccess userAccess) throws EncryptionException {
        return new Encoded(userAccess.getEntityId(), userAccess.getDatabaseId(),
                userAccess.getSourceUserId(), userAccess.getUserId(),
                userAccess.getSourceSystemId(), userAccess.getSystemId());
    }

    public static Encoded encode(Long entityId, Long databaseId, String userId, Long systemId) throws EncryptionException {
        Objects.requireNonNull(entityId);
        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(systemId);

        return new Encoded(entityId, databaseId, userId, systemId);
    }

    public static Encoded encode(Long entityId, Long databaseId, String userId, String targetUserId,
                                 Long systemId, Long targetSystemId) throws EncryptionException {
        Objects.requireNonNull(entityId);
        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(targetUserId);
        Objects.requireNonNull(systemId);
        Objects.requireNonNull(targetSystemId);

        return new Encoded(entityId, databaseId, userId, targetUserId, systemId, targetSystemId);
    }

    public static Decoded decode(String hash, String userId, Long systemId) throws EncryptionException {
        Objects.requireNonNull(hash);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(systemId);

        return new Decoded(hash, userId, systemId);
    }

    public static final class Encoded {

        private static final String ENCODE_PATTERN = "%s:%s";
        private static final String ENCODE_PATTERN_WITH_TARGET_INFO = "%s:%s:%s:%s";
        private final String hash;

        private Encoded(Long entityId, Long databaseId, String userId, Long systemId) throws EncryptionException {
            this(entityId, databaseId, userId, userId, systemId, systemId);
        }

        private Encoded(Long entityId, Long databaseId, String userId, String targetUserId,
                        Long systemId, Long targetSystemId) throws EncryptionException {
            String pass = String.format(PASS_PATTERN, userId, systemId);
            String value;
            if (withTargetInfo(systemId, targetSystemId)) {
                value = String.format(ENCODE_PATTERN_WITH_TARGET_INFO, entityId, databaseId,
                        targetSystemId, targetUserId);
            } else {
                value = String.format(ENCODE_PATTERN, entityId, databaseId);
            }
            hash = EncryptionUtils.encrypt(value, pass, BASE_64_OPTIONS);
        }

        private boolean withTargetInfo(Long systemId, Long targetSystemId) {
            return !systemId.equals(targetSystemId);
        }

        public String getHash() {
            return hash;
        }
    }

    public static final class Decoded {

        private Long entityId;
        private Long databaseId;
        private String targetUserId;
        private Long targetSystemId;

        public Decoded(String hash, String userId, Long systemId) throws EncryptionException {
            decode(hash, userId, systemId);
        }

        private void decode(String hash, String userId, Long systemId) throws EncryptionException {
            String pass = String.format(PASS_PATTERN, userId, systemId);
            String decoded = EncryptionUtils.decrypt(hash, pass, BASE_64_OPTIONS);
            String[] values = decoded.split(":");

            if (values.length < 2 || values.length > 4) {
                throw new IllegalArgumentException("Invalid hash content.");
            }

            if (!isNumber(values[0]) || !isNumber(values[1]) || (values.length > 2 && !isNumber(values[2]))) {
                throw new IllegalArgumentException("Invalid hash content type.");
            }

            this.entityId = Long.valueOf(values[0]);
            this.databaseId = Long.valueOf(values[1]);
            this.targetSystemId = values.length > 2 ? Long.valueOf(values[2]) : systemId;
            this.targetUserId = values.length > 2 ? String.valueOf(values[3]) : userId;
        }

        private boolean isNumber(String value) {
            byte[] values = value.getBytes();
            for (byte b : values) {
                if (!Character.isDigit(b)) {
                    return false;
                }
            }

            return true;
        }

        public UserAccess getUserAccess() {
            return UserAccess.of(targetSystemId, entityId, databaseId, targetUserId);
        }

        public Long getDatabaseId() {
            return databaseId;
        }

        public Long getEntityId() {
            return entityId;
        }
    }
}
