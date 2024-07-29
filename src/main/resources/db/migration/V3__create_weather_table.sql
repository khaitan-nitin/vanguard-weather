CREATE TABLE weather (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    raw_data TEXT,
    `description` VARCHAR(500),
    requested_on DATE,
    is_active BOOLEAN DEFAULT TRUE,
    flag BOOLEAN DEFAULT FALSE,
    created_on TIMESTAMP DEFAULT NOW(),
    created_by BIGINT,
    modified_on TIMESTAMP DEFAULT NOW(),
    modified_by BIGINT
);

CREATE INDEX idx_country_city_timestamp ON `weather` (`country`, `city`, `created_on`);