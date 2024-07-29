CREATE TABLE api_key (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_key VARCHAR(255) UNIQUE NOT NULL,
    rate_limit INT DEFAULT 5,
    request_count INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    flag BOOLEAN DEFAULT FALSE,
    created_on TIMESTAMP DEFAULT NOW(),
    created_by BIGINT,
    modified_on TIMESTAMP DEFAULT NOW(),
    modified_by BIGINT
);
 
CREATE INDEX idx_apiKey_is_active ON `api_key` (`api_key`, `is_active`);
