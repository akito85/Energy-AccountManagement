#!/bin/bash

###############################################################################
# API Testing Script for Account Management Module
#
# This script tests both Service Request and Relationship APIs
# Usage: ./test-api.sh
###############################################################################

set -e  # Exit on error

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
BASE_URL="${BASE_URL:-http://localhost:8080}"
TOKEN="${TOKEN:-your-jwt-token-here}"
ACCOUNT_ID="${ACCOUNT_ID:-12345}"

echo -e "${BLUE}╔═══════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║   Account Management API Testing Script              ║${NC}"
echo -e "${BLUE}╚═══════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${YELLOW}Configuration:${NC}"
echo -e "  BASE_URL: ${BASE_URL}"
echo -e "  ACCOUNT_ID: ${ACCOUNT_ID}"
echo -e "  TOKEN: ${TOKEN:0:20}...${NC}"
echo ""

# Function to make API call and display result
api_call() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4

    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${YELLOW}Test: ${description}${NC}"
    echo -e "${BLUE}Method: ${method} | Endpoint: ${endpoint}${NC}"
    echo ""

    if [ "$method" == "GET" ] || [ "$method" == "DELETE" ]; then
        response=$(curl -s -X $method "${BASE_URL}${endpoint}" \
            -H "Authorization: Bearer ${TOKEN}" \
            -H "Content-Type: application/json" \
            -w "\nHTTP_STATUS:%{http_code}")
    else
        response=$(curl -s -X $method "${BASE_URL}${endpoint}" \
            -H "Authorization: Bearer ${TOKEN}" \
            -H "Content-Type: application/json" \
            -d "$data" \
            -w "\nHTTP_STATUS:%{http_code}")
    fi

    http_status=$(echo "$response" | grep "HTTP_STATUS:" | cut -d: -f2)
    body=$(echo "$response" | sed '/HTTP_STATUS:/d')

    if [ "$http_status" -ge 200 ] && [ "$http_status" -lt 300 ]; then
        echo -e "${GREEN}✓ Success (HTTP ${http_status})${NC}"
    else
        echo -e "${RED}✗ Failed (HTTP ${http_status})${NC}"
    fi

    echo ""
    echo -e "${YELLOW}Response:${NC}"
    echo "$body" | jq '.' 2>/dev/null || echo "$body"
    echo ""

    # Extract and return ID if present
    echo "$body" | jq -r '.data.serviceRequestId // .data.id // empty' 2>/dev/null
}

###############################################################################
# SERVICE REQUEST API TESTS
###############################################################################

echo -e "${GREEN}═══════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}  SERVICE REQUEST API TESTS${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════════${NC}"
echo ""

# Test 1: Get Service Request Types
api_call "GET" "/v1/dbs/api/account/servicerequest/types" "" "Get Service Request Types"

# Test 2: Get Service Request Categories
api_call "GET" "/v1/dbs/api/account/servicerequest/category" "" "Get Service Request Categories"

# Test 3: Get Service Request Subcategories
api_call "GET" "/v1/dbs/api/account/servicerequest/subcategory" "" "Get Service Request Subcategories"

# Test 4: Create Service Request
REQUEST_NUMBER="SR-TEST-$(date +%s)"
SR_ID=$(api_call "POST" "/v1/dbs/api/account/servicerequest/create" \
'{
  "accountId": '"${ACCOUNT_ID}"',
  "requestNumber": "'"${REQUEST_NUMBER}"'",
  "requestType": 1001,
  "requestCategory": 2001,
  "priority": 3001,
  "subject": "Automated Test Service Request",
  "description": "This is an automated test request",
  "dueDate": "2024-12-31"
}' "Create Service Request")

if [ -n "$SR_ID" ]; then
    echo -e "${GREEN}Created Service Request ID: ${SR_ID}${NC}"
    echo ""

    # Test 5: Get Service Request Detail
    api_call "GET" "/v1/dbs/api/account/servicerequest/detail/${SR_ID}" "" "Get Service Request Detail"

    # Test 6: Update Service Request
    api_call "PUT" "/v1/dbs/api/account/servicerequest/update" \
    '{
      "serviceRequestId": '"${SR_ID}"',
      "subject": "Updated Test Service Request",
      "description": "Updated description via automated test",
      "priority": 3002
    }' "Update Service Request"

    # Test 7: Get Service Request List
    api_call "GET" "/v1/dbs/api/account/servicerequest/list/${ACCOUNT_ID}?page=0&size=5" "" "Get Service Request List"

    # Test 8: Toggle Service Request Status
    api_call "PUT" "/v1/dbs/api/account/servicerequest/toggle/${SR_ID}" "" "Toggle Service Request Status"
fi

# Test 9: Composite Submit (Validation Only)
api_call "POST" "/v1/dbs/api/account/servicerequest/composite/validate" \
'{
  "serviceRequest": {
    "accountId": '"${ACCOUNT_ID}"',
    "requestNumber": "SR-COMP-'"$(date +%s)"'",
    "requestType": 1001,
    "requestCategory": 2001,
    "priority": 3001,
    "subject": "Composite Test Request",
    "dueDate": "2024-12-31"
  },
  "contacts": [
    {
      "contactType": "PRIMARY",
      "firstName": "Test",
      "lastName": "User",
      "email": "test@example.com",
      "phone": "+628123456789",
      "isPrimary": true
    }
  ],
  "validateOnly": true,
  "submitForApproval": false
}' "Composite Submission (Validation)"

###############################################################################
# RELATIONSHIP API TESTS
###############################################################################

echo -e "${GREEN}═══════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}  RELATIONSHIP API TESTS${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════════${NC}"
echo ""

# Test 1: Get Relationship Types (PARTY)
api_call "GET" "/v1/dbs/api/accounts/${ACCOUNT_ID}/relationship-types?category=PARTY" "" "Get Relationship Types (PARTY)"

# Test 2: Get Relationship Types (ACCOUNT)
api_call "GET" "/v1/dbs/api/accounts/${ACCOUNT_ID}/relationship-types?category=ACCOUNT" "" "Get Relationship Types (ACCOUNT)"

# Test 3: Get Relationship Types (ALL)
api_call "GET" "/v1/dbs/api/accounts/${ACCOUNT_ID}/relationship-types?category=ALL" "" "Get Relationship Types (ALL)"

# Test 4: Create Relationship
REL_ID=$(api_call "POST" "/v1/dbs/api/accounts/${ACCOUNT_ID}/relationships/create" \
'{
  "directionalFlag": "Y",
  "relationshipType": "FAMILY",
  "relationshipCategory": "PARTY",
  "objectId": 5001,
  "objectName": "Test Contact",
  "objectValue": "SPOUSE",
  "startDate": "2024-11-22",
  "description": "Automated test relationship"
}' "Create Relationship")

if [ -n "$REL_ID" ]; then
    echo -e "${GREEN}Created Relationship ID: ${REL_ID}${NC}"
    echo ""

    # Test 5: Get Relationship Detail
    api_call "GET" "/v1/dbs/api/accounts/${ACCOUNT_ID}/relationships/${REL_ID}" "" "Get Relationship Detail"

    # Test 6: Get Relationship List
    api_call "POST" "/v1/dbs/api/accounts/${ACCOUNT_ID}/relationships" \
    '{
      "search": "",
      "page": 0,
      "size": 5
    }' "Get Relationship List"

    # Test 7: Update Relationship
    api_call "PUT" "/v1/dbs/api/accounts/${ACCOUNT_ID}/relationships/${REL_ID}" \
    '{
      "objectValue": "PRIMARY_CONTACT",
      "description": "Updated via automated test"
    }' "Update Relationship"

    # Test 8: Delete Relationship
    api_call "DELETE" "/v1/dbs/api/accounts/${ACCOUNT_ID}/relationships/${REL_ID}" "" "Delete Relationship"
fi

###############################################################################
# TEST SUMMARY
###############################################################################

echo -e "${GREEN}═══════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}  TEST EXECUTION COMPLETED${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════════${NC}"
echo ""
echo -e "${YELLOW}Summary:${NC}"
echo -e "  All API endpoints have been tested"
echo -e "  Check the output above for detailed results"
echo ""
echo -e "${BLUE}Note: Some tests may fail if:${NC}"
echo -e "  - Application is not running"
echo -e "  - JWT token is invalid or expired"
echo -e "  - Account ID does not exist"
echo -e "  - Database connection issues"
echo ""
echo -e "${YELLOW}For detailed API documentation, see: API_DOCUMENTATION.md${NC}"
echo ""
