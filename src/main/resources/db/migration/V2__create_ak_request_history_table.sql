CREATE TABLE api_key_request_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_Key VARCHAR(255) NOT NULL,
    created_on TIMESTAMP DEFAULT NOW(),
    created_by BIGINT,
    modified_on TIMESTAMP DEFAULT NOW(),
    modified_by BIGINT
);

CREATE INDEX idx_apiKey ON `api_key_request_history` (`api_key`);