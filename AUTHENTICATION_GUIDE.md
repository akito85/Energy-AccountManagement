# Authentication Guide - Proper Login Flow

## Overview

This document explains the proper way to authenticate and login to the Energy Account Management system, especially when the `/auth/login` endpoint fails.

**System:** PGN Energy Account Management
**Authentication:** JWT (JSON Web Token)
**Password Encryption:** BCrypt
**Database:** PGNBILL (Oracle 19c)

---

## Table of Contents

- [Authentication Endpoints](#authentication-endpoints)
- [Login Request Format](#login-request-format)
- [Password Requirements](#password-requirements)
- [Common Login Failures](#common-login-failures)
- [Troubleshooting Guide](#troubleshooting-guide)
- [Security Configuration](#security-configuration)
- [Testing Authentication](#testing-authentication)

---

## Authentication Endpoints

Based on the security configuration, the system supports multiple authentication endpoints:

### Primary Login Endpoints

| Endpoint | Method | Purpose | Module |
|----------|--------|---------|--------|
| `/v1/dbs/api/auth/login` | POST | Standard user login | Main API |
| `/v1/dbs/api/auth/login-su` | POST | Super user login | Main API |
| `/v1/dbs/api/auth/relogin` | POST | Token refresh/re-authentication | Main API |
| `/um/v1/dbs/api/auth/**` | POST | User Management module auth | User Management |

### Whitelisted Endpoints (No Authentication Required)

The following endpoints are accessible without authentication:
- `/v1/dbs/api/auth/**` - All auth endpoints
- `/um/v1/dbs/api/auth/**` - User management auth endpoints
- `/swagger-ui/**` - API documentation
- `/v3/api-docs` - OpenAPI specs

---

## Login Request Format

### Standard Login Request

```http
POST /v1/dbs/api/auth/login
Content-Type: application/json

{
  "username": "your-username",
  "password": "your-password"
}
```

### Super User Login Request

```http
POST /v1/dbs/api/auth/login-su
Content-Type: application/json

{
  "username": "admin-username",
  "password": "admin-password"
}
```

### Expected Successful Response

```json
{
  "success": true,
  "httpCode": 200,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "userId": 245,
    "username": "omni",
    "email": "omni@pgn.com",
    "userLevel": "SU",
    "groupAccess": ["SUPER USER"],
    "entities": [1, 7],
    "expiresIn": 2147483647
  }
}
```

### Using the Token

After successful login, include the token in all subsequent requests:

```http
GET /v1/dbs/api/account/servicerequest/list/12345
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Password Requirements

### Password Encryption

The system uses **BCrypt** for password hashing with the following configuration:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### Password Storage in Database

```sql
-- M_USER table password field
PASSWORD VARCHAR2(255)  -- Stores BCrypt hash
```

Example BCrypt hash:
```
$2a$10$rGw.QxG5Q5P5Z5Z5Z5Z5ZOeQ5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5
```

### Generating BCrypt Password

**Node.js:**
```javascript
const bcrypt = require('bcryptjs');
const hash = bcrypt.hashSync('your-password', 10);
console.log(hash);
```

**Java:**
```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hashedPassword = encoder.encode("your-password");
```

**Online Tool:**
- https://bcrypt-generator.com/ (set rounds to 10)

---

## Common Login Failures

### 1. Endpoint Not Found (404)

**Error:**
```json
{
  "timestamp": "2024-11-22T18:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "No message available",
  "path": "/auth/login"
}
```

**Cause:** Missing `/v1/dbs/api` prefix

**Solution:** Use full endpoint path
```bash
# ❌ Wrong
curl -X POST http://localhost:8902/auth/login

# ✅ Correct
curl -X POST http://localhost:8902/v1/dbs/api/auth/login
```

### 2. Unauthorized (401)

**Error:**
```json
{
  "success": false,
  "httpCode": 401,
  "message": "Invalid username or password",
  "data": null
}
```

**Common Causes:**

#### A. User Does Not Exist

Check if user exists:
```sql
SELECT USER_ID, USERNAME, EMAIL, IS_ACTIVE, STATUS
FROM M_USER
WHERE USERNAME = 'your-username';
```

**Solution:** Create the user account (see CREATE_SUPER_USER_OMNI.sql)

#### B. Incorrect Password

The password in the database must be BCrypt encrypted.

**Solution:** Update password with correct hash
```sql
UPDATE M_USER
SET PASSWORD = '$2a$10$...'  -- BCrypt hash
WHERE USERNAME = 'your-username';
COMMIT;
```

#### C. Account Inactive

```sql
SELECT IS_ACTIVE, STATUS FROM M_USER WHERE USERNAME = 'your-username';
```

**Solution:** Activate the account
```sql
UPDATE M_USER
SET IS_ACTIVE = 'Y',
    STATUS = 'ACTIVE'
WHERE USERNAME = 'your-username';
COMMIT;
```

#### D. Account Locked

```sql
SELECT LOCKED_TIME, FAILED_LOGIN FROM M_USER WHERE USERNAME = 'your-username';
```

**Solution:** Unlock the account
```sql
UPDATE M_USER
SET LOCKED_TIME = NULL,
    FAILED_LOGIN = 0
WHERE USERNAME = 'your-username';
COMMIT;
```

### 3. Password Expired

**Error:**
```json
{
  "success": false,
  "httpCode": 401,
  "message": "Password has expired",
  "data": null
}
```

**Check:**
```sql
SELECT EXP_PASS FROM M_USER WHERE USERNAME = 'your-username';
```

**Solution:** Extend password expiration
```sql
UPDATE M_USER
SET EXP_PASS = ADD_MONTHS(SYSDATE, 12)  -- Extend 1 year
WHERE USERNAME = 'your-username';
COMMIT;
```

### 4. Wrong Authentication Type

**Check:**
```sql
SELECT AUTH_TYPE FROM M_USER WHERE USERNAME = 'your-username';
```

Expected values:
- `LOCAL` - Standard database authentication
- `LDAP` - LDAP/Active Directory authentication

**Solution:** Ensure AUTH_TYPE matches your authentication method
```sql
UPDATE M_USER
SET AUTH_TYPE = 'LOCAL'
WHERE USERNAME = 'your-username';
COMMIT;
```

### 5. Connection Refused / Service Unavailable (503)

**Error:**
```
Failed to connect to localhost:8902
```

**Causes:**
- Application server not running
- Wrong port number
- Firewall blocking connection

**Solution:**

1. **Check if application is running:**
   ```bash
   # Check Java process
   ps aux | grep java

   # Check port
   netstat -an | grep 8902
   ```

2. **Start the application:**
   ```bash
   cd dbs-module-account-management
   mvn spring-boot:run
   ```

3. **Verify configuration:**
   ```yaml
   # application-staging.yml
   server:
     port: 8902
   ```

### 6. CORS Error (Browser)

**Error:**
```
Access to XMLHttpRequest at 'http://localhost:8902/v1/dbs/api/auth/login'
from origin 'http://localhost:3000' has been blocked by CORS policy
```

**Cause:** Cross-Origin Resource Sharing not configured

**Solution:** The backend already enables CORS in WebSecurityConfig:
```java
http.cors().and().csrf().disable()
```

Ensure your client sends proper headers:
```javascript
fetch('http://localhost:8902/v1/dbs/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({ username: 'omni', password: 'password123' })
})
```

---

## Troubleshooting Guide

### Step 1: Verify Application is Running

```bash
# Check if Spring Boot is running
curl http://localhost:8902/actuator/health

# Or check any whitelisted endpoint
curl http://localhost:8902/swagger-ui/index.html
```

### Step 2: Test Authentication Endpoint

```bash
# Test with curl
curl -v -X POST http://localhost:8902/v1/dbs/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "omni",
    "password": "omni123"
  }'
```

**Expected Response:** HTTP 200 with JWT token

**If 404:** Check endpoint path and application logs

**If 401:** Verify credentials and user status

### Step 3: Check User Account Status

```sql
SELECT
    USER_ID,
    USERNAME,
    EMAIL,
    IS_ACTIVE,
    STATUS,
    AUTH_TYPE,
    FAILED_LOGIN,
    LOCKED_TIME,
    EXP_PASS,
    START_DATE,
    END_DATE
FROM M_USER
WHERE USERNAME = 'your-username';
```

**Required for successful login:**
- `IS_ACTIVE` = 'Y'
- `STATUS` = 'ACTIVE'
- `AUTH_TYPE` = 'LOCAL'
- `LOCKED_TIME` = NULL
- `EXP_PASS` > SYSDATE
- `START_DATE` <= SYSDATE
- `END_DATE` = NULL or > SYSDATE

### Step 4: Verify Password Hash

```bash
# Generate test hash
node -e "console.log(require('bcryptjs').hashSync('omni123', 10))"

# Output example:
# $2a$10$abc123...xyz789
```

Update user password:
```sql
UPDATE M_USER
SET PASSWORD = '$2a$10$abc123...xyz789'
WHERE USERNAME = 'omni';
COMMIT;
```

### Step 5: Check Application Logs

```bash
# View Spring Boot logs
tail -f logs/application.log

# Look for authentication errors
grep -i "authentication\|login\|401" logs/application.log
```

### Step 6: Verify JWT Configuration

Check `application-staging.yml`:
```yaml
backend:
  app:
    jwt:
      secret: 8xds//H4wsYnk+yYleprfr3uajuXN9svF8jVz4OrllQ=hjk76klz//ok891=fflkjk4hjG7jsadJn
      expiration: 2147483647  # Token validity in milliseconds
```

### Step 7: Test Token Validation

After getting a token, test if it works:
```bash
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

curl -X GET http://localhost:8902/v1/dbs/api/account/servicerequest/types \
  -H "Authorization: Bearer $TOKEN"
```

---

## Security Configuration

### Spring Security Setup

The system uses the following security configuration:

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers(AUTH_WHITELIST).permitAll()
            .anyRequest().authenticated();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
```

### JWT Token Structure

JWT tokens consist of three parts:
1. **Header** - Algorithm and token type
2. **Payload** - User information and claims
3. **Signature** - Verification signature

Example decoded token:
```json
{
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "sub": "omni",
    "userId": 245,
    "userLevel": "SU",
    "iat": 1700657400,
    "exp": 3848141047
  }
}
```

### Session Management

The system uses **stateless** session management:
- No server-side session storage
- All authentication via JWT tokens
- Token must be sent with every request
- Token validity: ~68 years (2147483647ms)

---

## Testing Authentication

### Complete Test Script

Save as `test-login.sh`:
```bash
#!/bin/bash

BASE_URL="http://localhost:8902"
USERNAME="omni"
PASSWORD="omni123"

echo "========================================="
echo "Testing Authentication Flow"
echo "========================================="
echo ""

# Test 1: Login
echo "1. Testing Login..."
RESPONSE=$(curl -s -X POST "${BASE_URL}/v1/dbs/api/auth/login" \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"${USERNAME}\",
    \"password\": \"${PASSWORD}\"
  }")

echo "Response: $RESPONSE"
echo ""

# Extract token
TOKEN=$(echo $RESPONSE | jq -r '.data.token // empty')

if [ -z "$TOKEN" ]; then
    echo "❌ Login failed - no token received"
    exit 1
fi

echo "✅ Login successful"
echo "Token: ${TOKEN:0:50}..."
echo ""

# Test 2: Use token to access protected endpoint
echo "2. Testing Token Authentication..."
API_RESPONSE=$(curl -s -X GET "${BASE_URL}/v1/dbs/api/account/servicerequest/types" \
  -H "Authorization: Bearer ${TOKEN}")

echo "Response: $API_RESPONSE"
echo ""

if echo "$API_RESPONSE" | jq -e '.success == true' > /dev/null 2>&1; then
    echo "✅ Token authentication successful"
else
    echo "❌ Token authentication failed"
    exit 1
fi

echo ""
echo "========================================="
echo "All authentication tests passed!"
echo "========================================="
```

Make executable and run:
```bash
chmod +x test-login.sh
./test-login.sh
```

### Manual cURL Tests

**Test 1: Login with Valid Credentials**
```bash
curl -v -X POST http://localhost:8902/v1/dbs/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "omni",
    "password": "omni123"
  }'
```

**Expected:** HTTP 200, JWT token in response

**Test 2: Login with Invalid Credentials**
```bash
curl -v -X POST http://localhost:8902/v1/dbs/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "omni",
    "password": "wrongpassword"
  }'
```

**Expected:** HTTP 401 Unauthorized

**Test 3: Access Protected Endpoint Without Token**
```bash
curl -v -X GET http://localhost:8902/v1/dbs/api/account/servicerequest/types
```

**Expected:** HTTP 401 Unauthorized

**Test 4: Access Protected Endpoint With Token**
```bash
TOKEN="your-jwt-token-here"

curl -v -X GET http://localhost:8902/v1/dbs/api/account/servicerequest/types \
  -H "Authorization: Bearer $TOKEN"
```

**Expected:** HTTP 200, data returned

---

## Database Queries for Debugging

### Check User Credentials

```sql
-- Find user by username
SELECT
    USER_ID,
    USERNAME,
    EMAIL,
    PASSWORD,  -- BCrypt hash
    IS_ACTIVE,
    STATUS,
    USER_LEVEL,
    AUTH_TYPE,
    FAILED_LOGIN,
    LOCKED_TIME,
    EXP_PASS,
    START_DATE,
    END_DATE
FROM M_USER
WHERE LOWER(USERNAME) = LOWER('omni');
```

### Check Login History

```sql
-- Recent login attempts
SELECT
    USERNAME,
    DATE_ACCESS,
    REMOTER_ADDR as IP_ADDRESS,
    IS_LOGIN,
    SESSION_ID
FROM LOG_USER_LOGIN
WHERE USERNAME = 'omni'
ORDER BY DATE_ACCESS DESC
FETCH FIRST 10 ROWS ONLY;
```

### Find All Active Users

```sql
SELECT
    USERNAME,
    EMAIL,
    USER_LEVEL,
    STATUS,
    IS_ACTIVE
FROM M_USER
WHERE STATUS = 'ACTIVE'
  AND IS_ACTIVE = 'Y'
ORDER BY USERNAME;
```

### Check Group Access

```sql
SELECT
    u.USERNAME,
    ga.NAME as GROUP_NAME,
    ga.USER_LEVEL,
    uga.STATUS,
    uga.START_DATE,
    uga.END_DATE
FROM M_USER u
JOIN T_USER_GROUPACCESS uga ON u.USER_ID = uga.USER_ID
JOIN M_GROUPACCESS ga ON uga.GA_ID = ga.GA_ID
WHERE u.USERNAME = 'omni'
  AND uga.STATUS = 'ACTIVE';
```

---

## Quick Reference

### Correct Login Endpoints

| Purpose | Endpoint | Port |
|---------|----------|------|
| Standard Login | `POST /v1/dbs/api/auth/login` | 8902 |
| Super User Login | `POST /v1/dbs/api/auth/login-su` | 8902 |
| Token Refresh | `POST /v1/dbs/api/auth/relogin` | 8902 |

### Required Request Headers

```
Content-Type: application/json
```

### Required Request Body

```json
{
  "username": "string",
  "password": "string"
}
```

### Required Response Headers for Protected APIs

```
Authorization: Bearer <jwt-token>
```

### User Account Requirements

- ✅ `IS_ACTIVE` = 'Y'
- ✅ `STATUS` = 'ACTIVE'
- ✅ `AUTH_TYPE` = 'LOCAL'
- ✅ `PASSWORD` = BCrypt hash
- ✅ `LOCKED_TIME` = NULL
- ✅ `EXP_PASS` > current date
- ✅ `FAILED_LOGIN` < max attempts

---

## Summary

### Proper Login Flow

1. **Prepare Request**
   - Endpoint: `POST http://localhost:8902/v1/dbs/api/auth/login`
   - Header: `Content-Type: application/json`
   - Body: `{"username": "omni", "password": "omni123"}`

2. **Send Request**
   - Use cURL, Postman, or HTTP client
   - Ensure application is running on port 8902

3. **Receive Token**
   - Extract JWT token from response
   - Store token securely

4. **Use Token**
   - Include in all subsequent requests
   - Header: `Authorization: Bearer <token>`

5. **Monitor**
   - Check login logs in `LOG_USER_LOGIN` table
   - Monitor failed login attempts

### If Login Fails

1. ✅ Verify endpoint path includes `/v1/dbs/api` prefix
2. ✅ Check application is running on correct port (8902)
3. ✅ Confirm user exists in M_USER table
4. ✅ Verify password is BCrypt encrypted
5. ✅ Check user status is ACTIVE
6. ✅ Ensure account is not locked
7. ✅ Verify password hasn't expired
8. ✅ Review application logs for errors

---

**Document Version:** 1.0
**Last Updated:** November 22, 2024
**Related Documents:**
- CREATE_SUPER_USER_OMNI.sql
- SUPER_USER_CREATION_SUMMARY.md
- API_DOCUMENTATION.md
