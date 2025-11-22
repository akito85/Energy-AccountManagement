# Super User "omni" Creation Summary

## Overview

This document details the complete process of creating a super user account "omni" with email `omni@pgn.com` that has **unrestricted access** to all system functionalities.

**Created:** November 22, 2024
**Status:** ✅ SQL Script Ready for Execution
**Database:** PGNBILL (Oracle 19c)

---

## User Profile

| Property | Value |
|----------|-------|
| **Username** | `omni` |
| **Email** | `omni@pgn.com` |
| **User Code** | `OMNI` |
| **User Level** | `SU` (Super User) |
| **User Type** | `EMP` (Employee) |
| **Authentication Type** | `LOCAL` |
| **Status** | `ACTIVE` |
| **Is Active** | `Y` |
| **Entity Access** | `ALL` (ENTITY_ID = 1) |
| **Group Access** | `SUPER USER` (GA_ID = 1) |
| **Phone** | `+628123456789` (configurable) |

---

## Access Capabilities

### ✅ 1. Full SUPER USER Access

The user is assigned to **Group Access ID = 1 (SUPER USER)** which provides:

- **User Level:** `SU` (Super User)
- **Total Menus:** 26 active menus
- **Total Actions:** 113 active actions
- **Access Scope:** All modules across the entire system

### ✅ 2. Submitter Capabilities

Can create and submit all types of records:
- ✓ Create new accounts
- ✓ Create service requests
- ✓ Submit for approval
- ✓ Create master data
- ✓ Upload documents
- ✓ Initiate workflows
- ✓ Perform all CREATE operations

### ✅ 3. Approver Capabilities

Can approve all requests and workflows:
- ✓ Approve service requests
- ✓ Approve master data changes
- ✓ Approve financial transactions
- ✓ Approve account modifications
- ✓ Override approval hierarchies (as SU)
- ✓ Perform all APPROVAL operations

### ✅ 4. Access to All Departments and Entities

**Entity Access Level: ALL (ENTITY_ID = 1)**

This grants access to all organizational entities:
- ✓ ALL (1) - Global access
- ✓ PGN (7)
- ✓ PGAS (421)
- ✓ PGASSOL (494)
- ✓ PGN LNG (569)
- ✓ PATRAJASA (572)

### ✅ 5. No System Restrictions

The SUPER USER role bypasses all system restrictions:
- ✓ No workflow limitations
- ✓ No approval hierarchy constraints
- ✓ No data filtering restrictions
- ✓ No time-based access limitations
- ✓ No cost center restrictions
- ✓ Full administrative privileges

### ✅ 6. Multi-Endpoint Login Support

The user can authenticate via multiple endpoints:

#### Standard Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "omni",
  "password": "your-password"
}
```

#### Super User Login
```http
POST /auth/login-su
Content-Type: application/json

{
  "username": "omni",
  "password": "your-password"
}
```

#### Session Relogin (Token Refresh)
```http
POST /auth/relogin
Authorization: Bearer <existing-token>
```

### ✅ 7. Complete Menu and Action Access

#### Menu Access (26 Menus)
The user has access to ALL system menus including:
- Dashboard & Analytics
- Account Management
- Service Request Management
- Master Data Management
- Financial Management
- Billing Management
- Approval Management
- User Management
- Configuration & Settings
- Reports & Analytics
- And 16+ more modules

#### Action Access (113 Actions)
Full access to all action types:
- **VIEW:** View all records and details
- **CREATE:** Create new records
- **UPDATE:** Modify existing records
- **DELETE:** Remove records
- **APPROVE:** Approve workflows
- **REJECT:** Reject submissions
- **EXPORT:** Export data
- **IMPORT:** Import data
- **PRINT:** Generate reports
- **DOWNLOAD:** Download documents
- **UPLOAD:** Upload files
- **CUSTOM ACTIONS:** All custom action buttons

---

## Database Structure

### Primary Tables Involved

#### 1. M_USER (User Master Table)
Stores user account information.

**Key Fields:**
- `USER_ID` - Unique identifier (auto-generated)
- `USERNAME` - Login username (`omni`)
- `EMAIL` - User email (`omni@pgn.com`)
- `PASSWORD` - BCrypt encrypted password
- `USER_LEVEL` - Permission level (`SU`)
- `IS_ACTIVE` - Active status (`Y`)
- `STATUS` - Account status (`ACTIVE`)
- `ENTITY_ID` - Entity access (1 = ALL)
- `AUTH_TYPE` - Authentication method (`LOCAL`)

#### 2. T_USER_GROUPACCESS (User-Group Assignment)
Links users to access groups.

**Key Fields:**
- `USER_GA_ID` - Unique assignment ID
- `USER_ID` - Reference to M_USER
- `GA_ID` - Group Access ID (1 = SUPER USER)
- `STATUS` - Assignment status (`ACTIVE`)
- `START_DATE` - Access start date
- `END_DATE` - Access expiration (NULL = permanent)

#### 3. M_GROUPACCESS (Group Access Definition)
Defines access groups and their permissions.

**SUPER USER Group (GA_ID = 1):**
- `NAME` - "SUPER USER"
- `USER_LEVEL` - `SU`
- `DESCRIPTION` - "Group Access Super User"
- `ENTITY_ID` - 7 (PGN)
- `STATUS` - `ACTIVE`

#### 4. R_GROUPACCESS_MENU (Menu Permissions)
Maps groups to accessible menus.

**For SUPER USER:**
- 26 active menu assignments
- All menus marked as `ACTIVE`

#### 5. R_GROUPACCESS_ACTION (Action Permissions)
Maps groups to allowed actions per menu.

**For SUPER USER:**
- 113 active action assignments
- Covers all CRUD operations
- Includes all custom actions

---

## Permission Hierarchy

```
┌─────────────────────────────────────────────────────────────┐
│                     USER: omni                               │
│                 (omni@pgn.com)                               │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      │ assigned to
                      ▼
┌─────────────────────────────────────────────────────────────┐
│              GROUP ACCESS: SUPER USER (GA_ID=1)              │
│              USER_LEVEL: SU                                  │
└─────────────────────┬───────────────────────────────────────┘
                      │
        ┌─────────────┴─────────────┐
        │                           │
        ▼                           ▼
┌───────────────────┐     ┌────────────────────┐
│   26 MENUS        │     │   113 ACTIONS      │
│                   │     │                    │
│ • Dashboard       │     │ • VIEW             │
│ • Accounts        │     │ • CREATE           │
│ • Service Req     │     │ • UPDATE           │
│ • Master Data     │     │ • DELETE           │
│ • Financial       │     │ • APPROVE          │
│ • Billing         │     │ • REJECT           │
│ • Approvals       │     │ • EXPORT           │
│ • Users           │     │ • IMPORT           │
│ • Config          │     │ • PRINT            │
│ • Reports         │     │ • DOWNLOAD         │
│ • And 16 more...  │     │ • UPLOAD           │
│                   │     │ • And 102 more...  │
└───────────────────┘     └────────────────────┘
```

---

## API Endpoint Equivalent

While the SQL script creates the user directly in the database, here are the equivalent API calls if using the user management endpoints:

### Step 1: Create User via API

```http
POST /v1/dbs/api/user/create
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "userCode": "OMNI",
  "username": "omni",
  "password": "omni123",
  "email": "omni@pgn.com",
  "phone": "+628123456789",
  "entityId": 1,
  "isActive": "Y",
  "status": "ACTIVE",
  "userLevel": "SU",
  "userType": "EMP",
  "authType": "LOCAL",
  "description": "Super User with full access to all modules",
  "startDate": "2024-11-22",
  "endDate": null,
  "expPass": "2025-11-22"
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "httpCode": 201,
  "message": "User created successfully",
  "data": {
    "userId": 245,
    "username": "omni",
    "email": "omni@pgn.com",
    "userLevel": "SU",
    "status": "ACTIVE"
  }
}
```

### Step 2: Assign Group Access via API

```http
POST /v1/dbs/api/user/assign-group
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "userId": 245,
  "groupAccessId": 1,
  "startDate": "2024-11-22",
  "endDate": null,
  "status": "ACTIVE"
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "httpCode": 201,
  "message": "Group access assigned successfully",
  "data": {
    "userGaId": 1001,
    "userId": 245,
    "groupAccessId": 1,
    "groupAccessName": "SUPER USER",
    "menuCount": 26,
    "actionCount": 113
  }
}
```

---

## Execution Steps

### Option 1: Execute SQL Script (Recommended)

1. **Review the SQL script:**
   ```bash
   cat CREATE_SUPER_USER_OMNI.sql
   ```

2. **Generate BCrypt password hash:**
   ```bash
   # Use an online BCrypt generator or:
   # Node.js example:
   node -e "console.log(require('bcryptjs').hashSync('omni123', 10))"
   ```

3. **Update the password in SQL script:**
   Replace the placeholder password hash with the generated hash.

4. **Connect to Oracle database:**
   ```bash
   sqlplus username/password@pgnbill
   ```

5. **Execute the script:**
   ```sql
   @CREATE_SUPER_USER_OMNI.sql
   ```

6. **Verify creation:**
   ```sql
   SELECT USERNAME, EMAIL, USER_LEVEL, STATUS
   FROM M_USER
   WHERE USERNAME = 'omni';
   ```

### Option 2: Execute via API (if available)

1. **Obtain admin authentication token:**
   ```bash
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"admin123"}'
   ```

2. **Create user via API:**
   ```bash
   curl -X POST http://localhost:8080/v1/dbs/api/user/create \
     -H "Authorization: Bearer <admin-token>" \
     -H "Content-Type: application/json" \
     -d @create-user-omni.json
   ```

3. **Assign group access:**
   ```bash
   curl -X POST http://localhost:8080/v1/dbs/api/user/assign-group \
     -H "Authorization: Bearer <admin-token>" \
     -H "Content-Type: application/json" \
     -d @assign-group-omni.json
   ```

---

## Testing the Super User Account

### Test 1: Login Verification

#### Standard Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "omni",
    "password": "omni123"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userLevel": "SU",
  "groupAccess": ["SUPER USER"],
  "entities": ["ALL"]
}
```

#### Super User Login
```bash
curl -X POST http://localhost:8080/auth/login-su \
  -H "Content-Type: application/json" \
  -d '{
    "username": "omni",
    "password": "omni123"
  }'
```

### Test 2: Menu Access Verification

```bash
curl -X GET http://localhost:8080/v1/dbs/api/menu/user-menus \
  -H "Authorization: Bearer <omni-token>"
```

**Expected:** Should return all 26 menus

### Test 3: Create Permission Test

```bash
# Test creating a service request
curl -X POST http://localhost:8080/v1/dbs/api/account/servicerequest/create \
  -H "Authorization: Bearer <omni-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 12345,
    "requestNumber": "SR-TEST-001",
    "requestType": 1001,
    "requestCategory": 2001,
    "priority": 3001,
    "subject": "Test Super User Create"
  }'
```

**Expected:** ✅ 201 Created

### Test 4: Update Permission Test

```bash
# Test updating a service request
curl -X PUT http://localhost:8080/v1/dbs/api/account/servicerequest/update \
  -H "Authorization: Bearer <omni-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "serviceRequestId": 9876,
    "subject": "Updated by Super User"
  }'
```

**Expected:** ✅ 200 OK

### Test 5: Delete Permission Test

```bash
# Test deleting a relationship
curl -X DELETE http://localhost:8080/v1/dbs/api/accounts/12345/relationships/1001 \
  -H "Authorization: Bearer <omni-token>"
```

**Expected:** ✅ 200 OK

### Test 6: Approval Permission Test

```bash
# Test approving a request
curl -X POST http://localhost:8080/v1/dbs/api/approval/approve \
  -H "Authorization: Bearer <omni-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "approvalId": 5001,
    "action": "APPROVE",
    "remarks": "Approved by super user"
  }'
```

**Expected:** ✅ 200 OK

### Test 7: Multi-Entity Access Test

```bash
# Test accessing different entities
curl -X GET http://localhost:8080/v1/dbs/api/account/list?entityId=7 \
  -H "Authorization: Bearer <omni-token>"

curl -X GET http://localhost:8080/v1/dbs/api/account/list?entityId=421 \
  -H "Authorization: Bearer <omni-token>"
```

**Expected:** ✅ Both should return data

---

## Security Considerations

### ⚠️ Important Security Notes

1. **Password Strength:**
   - The default password "omni123" is for demonstration only
   - **Change to a strong password** with:
     - Minimum 12 characters
     - Mix of uppercase, lowercase, numbers, symbols
     - No dictionary words

2. **Password Encryption:**
   - Passwords MUST be stored using BCrypt encryption
   - Never store plaintext passwords
   - Use salt rounds = 10 or higher

3. **Access Monitoring:**
   - Monitor super user activity via `LOG_USER_LOGIN` table
   - Review audit trails regularly
   - Set up alerts for sensitive operations

4. **Session Management:**
   - Token expiration should be configured appropriately
   - Implement session timeout for inactive users
   - Support forced logout for security incidents

5. **Password Expiration:**
   - Set to 12 months by default (`EXP_PASS`)
   - Enforce periodic password changes
   - Notify user before expiration

6. **Multi-Factor Authentication (MFA):**
   - Consider enabling MFA for super user accounts
   - Use time-based OTP or hardware tokens
   - Implement in `AUTH_TYPE` field if supported

---

## Verification Queries

### Check User Creation

```sql
SELECT
    USER_ID,
    USERNAME,
    EMAIL,
    USER_LEVEL,
    IS_ACTIVE,
    STATUS,
    ENTITY_ID,
    AUTH_TYPE,
    CREATED_DATE
FROM M_USER
WHERE USERNAME = 'omni';
```

### Check Group Access Assignment

```sql
SELECT
    uga.USER_GA_ID,
    uga.USER_ID,
    uga.GA_ID,
    ga.NAME as GROUP_NAME,
    ga.USER_LEVEL,
    uga.STATUS,
    uga.START_DATE,
    uga.END_DATE
FROM T_USER_GROUPACCESS uga
JOIN M_GROUPACCESS ga ON uga.GA_ID = ga.GA_ID
WHERE uga.USER_ID = (SELECT USER_ID FROM M_USER WHERE USERNAME = 'omni');
```

### Check Menu Permissions

```sql
SELECT
    COUNT(*) as TOTAL_MENUS,
    COUNT(CASE WHEN STATUS = 'ACTIVE' THEN 1 END) as ACTIVE_MENUS
FROM R_GROUPACCESS_MENU
WHERE GA_ID = 1;
```

### Check Action Permissions

```sql
SELECT
    COUNT(*) as TOTAL_ACTIONS,
    COUNT(CASE WHEN STATUS = 'ACTIVE' THEN 1 END) as ACTIVE_ACTIONS
FROM R_GROUPACCESS_ACTION
WHERE GA_MENU_ID IN (SELECT GA_MENU_ID FROM R_GROUPACCESS_MENU WHERE GA_ID = 1);
```

### Check Login History

```sql
SELECT
    LOG_USER_LOGIN_ID,
    USERNAME,
    DATE_ACCESS,
    REMOTER_ADDR,
    IS_LOGIN,
    SESSION_ID
FROM LOG_USER_LOGIN
WHERE USERNAME = 'omni'
ORDER BY DATE_ACCESS DESC;
```

---

## Troubleshooting

### Issue 1: User Cannot Login

**Symptoms:**
- Login returns 401 Unauthorized
- "Invalid credentials" error

**Solutions:**
1. Verify user status:
   ```sql
   SELECT IS_ACTIVE, STATUS FROM M_USER WHERE USERNAME = 'omni';
   ```
   - `IS_ACTIVE` should be `'Y'`
   - `STATUS` should be `'ACTIVE'`

2. Check password hash is correct
3. Verify `AUTH_TYPE` is `'LOCAL'`
4. Check account is not locked (`LOCKED_TIME` should be NULL)

### Issue 2: Missing Menu Access

**Symptoms:**
- User can login but sees no menus
- Empty menu list returned

**Solutions:**
1. Verify group access assignment:
   ```sql
   SELECT * FROM T_USER_GROUPACCESS
   WHERE USER_ID = (SELECT USER_ID FROM M_USER WHERE USERNAME = 'omni');
   ```

2. Check group access is ACTIVE
3. Verify menu permissions exist for GA_ID = 1

### Issue 3: Permission Denied Errors

**Symptoms:**
- 403 Forbidden when accessing certain features
- "Insufficient permissions" errors

**Solutions:**
1. Verify USER_LEVEL is 'SU'
2. Check action permissions are assigned
3. Ensure entity access is set to 1 (ALL)

### Issue 4: Session Expires Too Quickly

**Symptoms:**
- Token expires within minutes
- Frequent relogin required

**Solutions:**
1. Check JWT token expiration configuration
2. Verify session timeout settings
3. Use `/auth/relogin` to refresh token

---

## Summary

### ✅ What Was Created

1. **User Account:**
   - Username: `omni`
   - Email: `omni@pgn.com`
   - User Level: `SU` (Super User)
   - Status: `ACTIVE`
   - Entity Access: `ALL` (1)

2. **Group Access Assignment:**
   - Group: `SUPER USER` (GA_ID = 1)
   - Status: `ACTIVE`
   - Duration: Permanent (no end date)

3. **Permissions Granted:**
   - **26 Menus** - Full access to all system modules
   - **113 Actions** - All CRUD operations + custom actions
   - **6 Entities** - Access to ALL organizational entities

4. **Capabilities Enabled:**
   - ✅ Full submitter rights (create all records)
   - ✅ Full approver rights (approve all workflows)
   - ✅ Administrative privileges
   - ✅ No system restrictions
   - ✅ Multi-endpoint login support
   - ✅ Complete menu and action access

### 📊 Access Statistics

| Metric | Value |
|--------|-------|
| Total Menus Accessible | 26 |
| Total Actions Available | 113 |
| Entities Accessible | 6 (ALL) |
| Group Access Level | SUPER USER (SU) |
| Approval Hierarchies | Bypass All |
| Data Filters | None |
| Cost Center Restrictions | None |
| Workflow Limitations | None |

### 🔐 Authentication Methods

The user can authenticate via:
1. `/auth/login` - Standard login endpoint
2. `/auth/login-su` - Super user specific login
3. `/auth/relogin` - Token refresh endpoint

### 📁 Files Created

1. `CREATE_SUPER_USER_OMNI.sql` - SQL script to create the user
2. `SUPER_USER_CREATION_SUMMARY.md` - This documentation

### 🎯 Next Steps

1. **Execute the SQL script** to create the user in the database
2. **Change the default password** to a secure password
3. **Test the login** via API endpoints
4. **Verify permissions** by testing CRUD operations
5. **Monitor usage** via login logs and audit trails
6. **Configure MFA** (if required by your security policy)

---

## Appendix: Database Schema Details

### M_USER Table Structure

```sql
CREATE TABLE M_USER (
    USER_ID NUMBER PRIMARY KEY,
    USER_CODE VARCHAR2(50),
    USERNAME VARCHAR2(100),
    PASSWORD VARCHAR2(255),
    EMAIL VARCHAR2(100),
    PHONE VARCHAR2(20),
    ENTITY_ID NUMBER,
    IS_ACTIVE CHAR(1),
    START_DATE DATE,
    END_DATE DATE,
    STATUS VARCHAR2(20),
    CREATED_DATE DATE,
    UPDATED_DATE DATE,
    CREATED_BY VARCHAR2(50),
    UPDATED_BY VARCHAR2(50),
    DESCRIPTION VARCHAR2(500),
    USER_LEVEL VARCHAR2(10),
    EMPLOYEE_ID NUMBER,
    USER_TYPE VARCHAR2(20),
    AUTH_TYPE VARCHAR2(20),
    FAILED_LOGIN NUMBER,
    LOCKED_TIME DATE,
    EXP_PASS DATE
);
```

### T_USER_GROUPACCESS Table Structure

```sql
CREATE TABLE T_USER_GROUPACCESS (
    USER_GA_ID NUMBER PRIMARY KEY,
    GA_ID NUMBER,
    START_DATE DATE,
    END_DATE DATE,
    STATUS VARCHAR2(20),
    CREATED_DATE DATE,
    UPDATED_DATE DATE,
    CREATED_BY VARCHAR2(50),
    UPDATED_BY VARCHAR2(50),
    USER_ID NUMBER,
    CONSTRAINT FK_UGA_USER FOREIGN KEY (USER_ID) REFERENCES M_USER(USER_ID),
    CONSTRAINT FK_UGA_GA FOREIGN KEY (GA_ID) REFERENCES M_GROUPACCESS(GA_ID)
);
```

### Permission Flow Diagram

```
M_USER (omni)
    ↓ (USER_ID)
T_USER_GROUPACCESS
    ↓ (GA_ID = 1)
M_GROUPACCESS (SUPER USER)
    ↓
R_GROUPACCESS_MENU (26 menus)
    ↓
R_GROUPACCESS_ACTION (113 actions)
    ↓
M_MENU & M_ACTION (Actual menu and action definitions)
```

---

**Document Version:** 1.0
**Last Updated:** November 22, 2024
**Author:** System Administrator
**Classification:** Internal - Restricted Access
