CREATE DATABASE IF NOT EXISTS reservecamping
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE reservecamping;

-- ===========================================================
-- TABLE ROLE
-- ===========================================================
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO role (id, name) VALUES
(1, 'SUPER_ADMIN'),
(2, 'CAMPING_ADMIN'),
(3, 'GESTIONNAIRE'),
(4, 'UTILISATEUR');

-- ===========================================================
-- TABLE COUNTRY
-- ===========================================================
CREATE TABLE IF NOT EXISTS country (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10) NOT NULL UNIQUE
);

INSERT INTO country (id, name, code) VALUES
(1, 'Canada', 'CA'),
(2, 'États-Unis', 'US'),
(3, 'Mexique', 'MX');

-- ===========================================================
-- TABLE PROVINCE / STATE
-- ===========================================================
CREATE TABLE IF NOT EXISTS province_state (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    country_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10) NOT NULL,
    FOREIGN KEY (country_id) REFERENCES country(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- CANADA
INSERT INTO province_state (country_id, name, code) VALUES
(1, 'Alberta', 'AB'),
(1, 'Colombie-Britannique', 'BC'),
(1, 'Île-du-Prince-Édouard', 'PE'),
(1, 'Manitoba', 'MB'),
(1, 'Nouveau-Brunswick', 'NB'),
(1, 'Nouvelle-Écosse', 'NS'),
(1, 'Nunavut', 'NU'),
(1, 'Ontario', 'ON'),
(1, 'Québec', 'QC'),
(1, 'Saskatchewan', 'SK'),
(1, 'Terre-Neuve-et-Labrador', 'NL'),
(1, 'Territoires du Nord-Ouest', 'NT'),
(1, 'Yukon', 'YT');

-- USA (50 États)
INSERT INTO province_state (country_id, name, code) VALUES
(2, 'Alabama', 'AL'),
(2, 'Alaska', 'AK'),
(2, 'Arizona', 'AZ'),
(2, 'Arkansas', 'AR'),
(2, 'Californie', 'CA'),
(2, 'Caroline du Nord', 'NC'),
(2, 'Caroline du Sud', 'SC'),
(2, 'Colorado', 'CO'),
(2, 'Connecticut', 'CT'),
(2, 'Dakota du Nord', 'ND'),
(2, 'Dakota du Sud', 'SD'),
(2, 'Delaware', 'DE'),
(2, 'Floride', 'FL'),
(2, 'Géorgie', 'GA'),
(2, 'Hawaï', 'HI'),
(2, 'Idaho', 'ID'),
(2, 'Illinois', 'IL'),
(2, 'Indiana', 'IN'),
(2, 'Iowa', 'IA'),
(2, 'Kansas', 'KS'),
(2, 'Kentucky', 'KY'),
(2, 'Louisiane', 'LA'),
(2, 'Maine', 'ME'),
(2, 'Maryland', 'MD'),
(2, 'Massachusetts', 'MA'),
(2, 'Michigan', 'MI'),
(2, 'Minnesota', 'MN'),
(2, 'Mississippi', 'MS'),
(2, 'Missouri', 'MO'),
(2, 'Montana', 'MT'),
(2, 'Nebraska', 'NE'),
(2, 'Nevada', 'NV'),
(2, 'New Hampshire', 'NH'),
(2, 'New Jersey', 'NJ'),
(2, 'New York', 'NY'),
(2, 'Nouveau-Mexique', 'NM'),
(2, 'Ohio', 'OH'),
(2, 'Oklahoma', 'OK'),
(2, 'Oregon', 'OR'),
(2, 'Pennsylvanie', 'PA'),
(2, 'Rhode Island', 'RI'),
(2, 'Tennessee', 'TN'),
(2, 'Texas', 'TX'),
(2, 'Utah', 'UT'),
(2, 'Vermont', 'VT'),
(2, 'Virginie', 'VA'),
(2, 'Virginie-Occidentale', 'WV'),
(2, 'Washington', 'WA'),
(2, 'Wisconsin', 'WI'),
(2, 'Wyoming', 'WY');

-- MEXIQUE
INSERT INTO province_state (country_id, name, code) VALUES
(3, 'Aguascalientes', 'AGS'),
(3, 'Baja California', 'BC'),
(3, 'Baja California Sur', 'BCS'),
(3, 'Campeche', 'CAM'),
(3, 'Chiapas', 'CHIS'),
(3, 'Chihuahua', 'CHIH'),
(3, 'Ciudad de México', 'CDMX'),
(3, 'Coahuila', 'COAH'),
(3, 'Colima', 'COL'),
(3, 'Durango', 'DGO'),
(3, 'Guanajuato', 'GTO'),
(3, 'Guerrero', 'GRO'),
(3, 'Hidalgo', 'HID'),
(3, 'Jalisco', 'JAL'),
(3, 'México', 'MEX'),
(3, 'Michoacán', 'MICH'),
(3, 'Morelos', 'MOR'),
(3, 'Nayarit', 'NAY'),
(3, 'Nuevo León', 'NL'),
(3, 'Oaxaca', 'OAX'),
(3, 'Puebla', 'PUE'),
(3, 'Querétaro', 'QRO'),
(3, 'Quintana Roo', 'QROO'),
(3, 'San Luis Potosí', 'SLP'),
(3, 'Sinaloa', 'SIN'),
(3, 'Sonora', 'SON'),
(3, 'Tabasco', 'TAB'),
(3, 'Tamaulipas', 'TAMPS'),
(3, 'Tlaxcala', 'TLAX'),
(3, 'Veracruz', 'VER'),
(3, 'Yucatán', 'YUC'),
(3, 'Zacatecas', 'ZAC');

-- ===========================================================
-- TABLE USERS
-- ===========================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    firstname VARCHAR(40) NOT NULL,
    lastname VARCHAR(40) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,

    phone VARCHAR(20),
    address VARCHAR(120),
    city VARCHAR(60),
    postalCode VARCHAR(15),

    equipmentType VARCHAR(30),
    equipmentLength INT,
    hasSlideOut BOOLEAN,

    role_id BIGINT,
    country_id BIGINT,
    province_state_id BIGINT,

    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (country_id) REFERENCES country(id),
    FOREIGN KEY (province_state_id) REFERENCES province_state(id)
);
