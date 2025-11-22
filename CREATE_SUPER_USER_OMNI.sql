-- ============================================================================
-- CREATE SUPER USER: OMNI
-- Email: omni@pgn.com
-- Full access to all modules, menus, actions, entities
-- ============================================================================

-- Step 1: Create the user in M_USER table
-- Password: Use BCrypt hash for "omni123" (you should change this)
-- Note: Password should be encrypted using BCrypt before insertion
INSERT INTO M_USER (
    USER_ID,
    USER_CODE,
    USERNAME,
    PASSWORD,
    EMAIL,
    PHONE,
    ENTITY_ID,
    IS_ACTIVE,
    START_DATE,
    END_DATE,
    STATUS,
    CREATED_DATE,
    UPDATED_DATE,
    CREATED_BY,
    UPDATED_BY,
    DESCRIPTION,
    USER_LEVEL,
    EMPLOYEE_ID,
    USER_TYPE,
    AUTH_TYPE,
    FAILED_LOGIN,
    LOCKED_TIME,
    EXP_PASS
) VALUES (
    (SELECT NVL(MAX(USER_ID), 0) + 1 FROM M_USER),  -- Auto-increment USER_ID
    'OMNI',                                          -- USER_CODE
    'omni',                                          -- USERNAME
    '$2a$10$rGw.QxG5Q5P5Z5Z5Z5Z5ZOeQ5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5',  -- PASSWORD (BCrypt hash - CHANGE THIS!)
    'omni@pgn.com',                                  -- EMAIL
    '+628123456789',                                 -- PHONE (optional)
    1,                                               -- ENTITY_ID (1 = ALL entities)
    'Y',                                             -- IS_ACTIVE
    SYSDATE,                                         -- START_DATE
    NULL,                                            -- END_DATE (NULL = no expiration)
    'ACTIVE',                                        -- STATUS
    SYSDATE,                                         -- CREATED_DATE
    NULL,                                            -- UPDATED_DATE
    'SYSTEM',                                        -- CREATED_BY
    NULL,                                            -- UPDATED_BY
    'Super User with full access to all modules',   -- DESCRIPTION
    'SU',                                            -- USER_LEVEL (SU = Super User)
    NULL,                                            -- EMPLOYEE_ID (NULL if not employee)
    'EMP',                                           -- USER_TYPE (EMP = Employee)
    'LOCAL',                                         -- AUTH_TYPE (LOCAL authentication)
    0,                                               -- FAILED_LOGIN
    NULL,                                            -- LOCKED_TIME
    ADD_MONTHS(SYSDATE, 12)                         -- EXP_PASS (1 year from now)
);

-- Step 2: Assign SUPER USER group access (GA_ID = 1)
-- This grants access to all 26 menus and 113 actions
INSERT INTO T_USER_GROUPACCESS (
    USER_GA_ID,
    GA_ID,
    START_DATE,
    END_DATE,
    STATUS,
    CREATED_DATE,
    UPDATED_DATE,
    CREATED_BY,
    UPDATED_BY,
    USER_ID
) VALUES (
    (SELECT NVL(MAX(USER_GA_ID), 0) + 1 FROM T_USER_GROUPACCESS),  -- Auto-increment
    1,                                                               -- GA_ID (1 = SUPER USER)
    SYSDATE,                                                         -- START_DATE
    NULL,                                                            -- END_DATE (permanent access)
    'ACTIVE',                                                        -- STATUS
    SYSDATE,                                                         -- CREATED_DATE
    NULL,                                                            -- UPDATED_DATE
    'SYSTEM',                                                        -- CREATED_BY
    NULL,                                                            -- UPDATED_BY
    (SELECT USER_ID FROM M_USER WHERE USERNAME = 'omni')           -- USER_ID (reference to created user)
);

-- Step 3: Grant access to ALL entities (optional, since ENTITY_ID = 1 already gives ALL)
-- This ensures the user can access all organizational entities
-- Note: SUPER USER with GA_ID=1 already has access to ENTITY_ID=7 (PGN)
-- The user record itself is set to ENTITY_ID=1 (ALL) which gives global access

-- Step 4: Verify the user was created successfully
SELECT
    u.USER_ID,
    u.USERNAME,
    u.EMAIL,
    u.USER_LEVEL,
    u.IS_ACTIVE,
    u.STATUS,
    u.ENTITY_ID,
    ga.NAME as GROUP_ACCESS_NAME,
    ga.USER_LEVEL as GA_LEVEL,
    uga.STATUS as GA_STATUS,
    (SELECT COUNT(*) FROM R_GROUPACCESS_MENU WHERE GA_ID = uga.GA_ID AND STATUS = 'ACTIVE') as MENU_COUNT,
    (SELECT COUNT(*) FROM R_GROUPACCESS_ACTION
     WHERE GA_MENU_ID IN (SELECT GA_MENU_ID FROM R_GROUPACCESS_MENU WHERE GA_ID = uga.GA_ID)
     AND STATUS = 'ACTIVE') as ACTION_COUNT
FROM M_USER u
LEFT JOIN T_USER_GROUPACCESS uga ON u.USER_ID = uga.USER_ID
LEFT JOIN M_GROUPACCESS ga ON uga.GA_ID = ga.GA_ID
WHERE u.USERNAME = 'omni';

-- ============================================================================
-- EXPECTED RESULT:
-- - USER_ID: (auto-generated)
-- - USERNAME: omni
-- - EMAIL: omni@pgn.com
-- - USER_LEVEL: SU (Super User)
-- - IS_ACTIVE: Y
-- - STATUS: ACTIVE
-- - ENTITY_ID: 1 (ALL)
-- - GROUP_ACCESS_NAME: SUPER USER
-- - GA_LEVEL: SU
-- - GA_STATUS: ACTIVE
-- - MENU_COUNT: 26
-- - ACTION_COUNT: 113
-- ============================================================================

-- Step 5: Query to see all permissions granted
SELECT
    m.NAME as MENU_NAME,
    m.PATH as MENU_PATH,
    a.NAME as ACTION_NAME,
    a.DESCRIPTION as ACTION_DESCRIPTION
FROM R_GROUPACCESS_MENU gam
JOIN M_MENU m ON gam.MENU_ID = m.MENU_ID
LEFT JOIN R_GROUPACCESS_ACTION gaa ON gam.GA_MENU_ID = gaa.GA_MENU_ID
LEFT JOIN M_ACTION a ON gaa.ACTION_ID = a.ACTION_ID
WHERE gam.GA_ID = 1
  AND gam.STATUS = 'ACTIVE'
  AND (gaa.STATUS = 'ACTIVE' OR gaa.STATUS IS NULL)
ORDER BY m.NAME, a.NAME;

COMMIT;

-- ============================================================================
-- NOTES:
-- 1. The password hash shown above is a placeholder. Generate a proper BCrypt
--    hash for the actual password before running this script.
-- 2. The user will have access to login via:
--    - /auth/login (standard login)
--    - /auth/login-su (super user login)
--    - /auth/relogin (session refresh)
-- 3. Permissions include:
--    - VIEW: All 26 menus
--    - CREATE: All creation actions
--    - UPDATE: All update actions
--    - DELETE: All delete actions
--    - APPROVE: All approval actions
--    - All custom action buttons (113 total actions)
-- 4. Entity access: ALL (ENTITY_ID = 1) provides access to:
--    - ALL (1)
--    - PGN (7)
--    - PGAS (421)
--    - PGASSOL (494)
--    - PGN LNG (569)
--    - PATRAJASA (572)
-- ============================================================================
