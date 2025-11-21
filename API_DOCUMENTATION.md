# Account Management API Documentation

## Table of Contents
- [Overview](#overview)
- [Base URL](#base-url)
- [Authentication](#authentication)
- [Service Request APIs](#service-request-apis)
- [Account Relationship APIs](#account-relationship-apis)
- [Testing Guide](#testing-guide)
- [Error Handling](#error-handling)

---

## Overview

This document provides comprehensive API documentation for the Account Management module, specifically covering:
- **Service Request Management**: Create, update, and manage service requests with full lifecycle support
- **Account Relationship Management**: Manage relationships between accounts and parties

## Base URL

```
Development: http://localhost:8080
Staging: https://staging-api.example.com
Production: https://api.example.com
```

All endpoints are prefixed with: `/v1/dbs/api`

## Authentication

All APIs require JWT authentication. Include the token in the Authorization header:

```http
Authorization: Bearer <your-jwt-token>
```

---

# Service Request APIs

## Endpoints Overview

### Base Path: `/v1/dbs/api/account/servicerequest`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/create` | Create new service request |
| PUT | `/update` | Update existing service request |
| GET | `/detail/{serviceRequestId}` | Get service request details |
| GET | `/list/{accountId}` | Get paginated list of service requests |
| PUT | `/toggle/{serviceRequestId}` | Toggle service request status (active/inactive) |
| GET | `/types` | Get service request types dropdown |
| GET | `/category` | Get service request categories dropdown |
| GET | `/subcategory` | Get service request subcategories dropdown |

### Composite Endpoints: `/v1/dbs/api/account/servicerequest/composite`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/submit-complete` | Submit complete service request in single transaction |
| POST | `/validate` | Validate request without saving |
| POST | `/save-draft` | Save incomplete request as draft |
| GET | `/status/{serviceRequestId}` | Get submission status |
| PUT | `/resume/{serviceRequestId}` | Resume incomplete submission |

---

## 1. Create Service Request

Creates a new service request.

### Endpoint
```http
POST /v1/dbs/api/account/servicerequest/create
```

### Request Body
```json
{
  "accountId": 12345,
  "requestNumber": "SR-2024-001",
  "requestType": 1001,
  "requestCategory": 2001,
  "priority": 3001,
  "subject": "New Gas Connection Request",
  "description": "Request for new gas connection installation",
  "requestedDate": "2024-11-22",
  "dueDate": "2024-12-15",
  "assignedTo": 5001
}
```

### Required Fields
- `accountId` (Integer): Account identifier
- `requestNumber` (String): Unique request number
- `requestType` (Integer): Request type ID from GLOBAL_TYPE
- `requestCategory` (Integer): Request category ID from GLOBAL_TYPE
- `priority` (Integer): Priority level ID from GLOBAL_TYPE
- `subject` (String): Service request subject

### Optional Fields
- `description` (String): Detailed description
- `requestedDate` (Date): Requested date (defaults to current date)
- `dueDate` (Date): Due date
- `assignedTo` (Integer): User ID to assign the request

### Response (201 Created)
```json
{
  "success": true,
  "httpCode": 201,
  "message": "Service request created successfully",
  "data": {
    "serviceRequestId": 9876,
    "accountId": 12345,
    "requestNumber": "SR-2024-001",
    "requestType": 1001,
    "requestCategory": 2001,
    "requestStatus": 4001,
    "priority": 3001,
    "subject": "New Gas Connection Request",
    "description": "Request for new gas connection installation",
    "requestedDate": "2024-11-22T00:00:00.000+00:00",
    "dueDate": "2024-12-15T00:00:00.000+00:00",
    "assignedTo": 5001,
    "status": "ACTIVE",
    "createdBy": "admin",
    "createdDate": "2024-11-22T06:30:00.000+00:00"
  }
}
```

### cURL Example
```bash
curl -X POST "http://localhost:8080/v1/dbs/api/account/servicerequest/create" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 12345,
    "requestNumber": "SR-2024-001",
    "requestType": 1001,
    "requestCategory": 2001,
    "priority": 3001,
    "subject": "New Gas Connection Request",
    "description": "Request for new gas connection installation",
    "dueDate": "2024-12-15"
  }'
```

---

## 2. Update Service Request

Updates an existing service request.

### Endpoint
```http
PUT /v1/dbs/api/account/servicerequest/update
```

### Request Body
```json
{
  "serviceRequestId": 9876,
  "subject": "Updated: New Gas Connection Request",
  "description": "Updated description with additional requirements",
  "dueDate": "2024-12-20",
  "completionDate": "2024-11-25",
  "assignedTo": 5002,
  "requestStatus": 4002,
  "priority": 3002
}
```

### Required Fields
- `serviceRequestId` (Integer): Service request ID to update

### Optional Fields (only include fields to update)
- `subject` (String): Updated subject
- `description` (String): Updated description
- `dueDate` (Date): Updated due date
- `completionDate` (Date): Completion date
- `assignedTo` (Integer): Reassign to different user
- `requestStatus` (Integer): Updated status
- `priority` (Integer): Updated priority

### Response (200 OK)
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Service request updated successfully",
  "data": {
    "serviceRequestId": 9876,
    "subject": "Updated: New Gas Connection Request",
    "description": "Updated description with additional requirements",
    "dueDate": "2024-12-20T00:00:00.000+00:00",
    "completionDate": "2024-11-25T00:00:00.000+00:00",
    "assignedTo": 5002,
    "requestStatus": 4002,
    "priority": 3002,
    "updatedBy": "admin",
    "updatedDate": "2024-11-22T07:00:00.000+00:00"
  }
}
```

### cURL Example
```bash
curl -X PUT "http://localhost:8080/v1/dbs/api/account/servicerequest/update" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "serviceRequestId": 9876,
    "subject": "Updated: New Gas Connection Request",
    "priority": 3002
  }'
```

---

## 3. Get Service Request Detail

Retrieves detailed information about a specific service request.

### Endpoint
```http
GET /v1/dbs/api/account/servicerequest/detail/{serviceRequestId}
```

### Path Parameters
- `serviceRequestId` (Integer): Service request identifier

### Response (200 OK)
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Success",
  "data": {
    "serviceRequestId": 9876,
    "accountId": 12345,
    "requestNumber": "SR-2024-001",
    "requestType": 1001,
    "requestTypeName": "New Connection",
    "requestCategory": 2001,
    "requestCategoryName": "Installation",
    "requestStatus": 4001,
    "requestStatusName": "NEW",
    "priority": 3001,
    "priorityName": "HIGH",
    "subject": "New Gas Connection Request",
    "description": "Request for new gas connection installation",
    "requestedDate": "2024-11-22T00:00:00.000+00:00",
    "dueDate": "2024-12-15T00:00:00.000+00:00",
    "completionDate": null,
    "assignedTo": 5001,
    "status": "ACTIVE",
    "createdBy": "admin",
    "createdDate": "2024-11-22T06:30:00.000+00:00",
    "updatedBy": null,
    "updatedDate": null
  }
}
```

### cURL Example
```bash
curl -X GET "http://localhost:8080/v1/dbs/api/account/servicerequest/detail/9876" \
  -H "Authorization: Bearer <token>"
```

---

## 4. Get Service Request List

Retrieves a paginated list of service requests for a specific account.

### Endpoint
```http
GET /v1/dbs/api/account/servicerequest/list/{accountId}?page=0&size=10&sort=createdDate,desc
```

### Path Parameters
- `accountId` (Integer): Account identifier

### Query Parameters
- `page` (Integer, default: 0): Page number (zero-indexed)
- `size` (Integer, default: 10): Number of items per page
- `sort` (String, optional): Sort criteria (e.g., "createdDate,desc")
- `search` (String, optional): Search term
- `filters` (Object, optional): Additional filter criteria

### Response (200 OK)
```json
{
  "page": {
    "size": 10,
    "totalElements": 25,
    "totalPages": 3,
    "number": 0
  },
  "content": [
    {
      "serviceRequestId": 9876,
      "requestNumber": "SR-2024-001",
      "subject": "New Gas Connection Request",
      "requestStatus": 4001,
      "priority": 3001,
      "requestedDate": "2024-11-22T00:00:00.000+00:00",
      "dueDate": "2024-12-15T00:00:00.000+00:00",
      "createdDate": "2024-11-22T06:30:00.000+00:00"
    }
  ]
}
```

### cURL Example
```bash
curl -X GET "http://localhost:8080/v1/dbs/api/account/servicerequest/list/12345?page=0&size=10&sort=createdDate,desc" \
  -H "Authorization: Bearer <token>"
```

---

## 5. Toggle Service Request Status

Toggles service request status between ACTIVE and INACTIVE.

### Endpoint
```http
PUT /v1/dbs/api/account/servicerequest/toggle/{serviceRequestId}
```

### Path Parameters
- `serviceRequestId` (Integer): Service request identifier

### Response (200 OK)
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Service request status updated successfully",
  "data": {
    "serviceRequestId": 9876,
    "status": "INACTIVE",
    "updatedBy": "admin",
    "updatedDate": "2024-11-22T08:00:00.000+00:00"
  }
}
```

### cURL Example
```bash
curl -X PUT "http://localhost:8080/v1/dbs/api/account/servicerequest/toggle/9876" \
  -H "Authorization: Bearer <token>"
```

---

## 6. Get Service Request Types

Retrieves all available service request types for dropdown selection.

### Endpoint
```http
GET /v1/dbs/api/account/servicerequest/types
```

### Response (200 OK)
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Success",
  "data": [
    {
      "glbTypeValId": 1001,
      "name": "New Connection",
      "glbValue": "NEW_CONNECTION",
      "description": "Request for new gas connection"
    },
    {
      "glbTypeValId": 1002,
      "name": "Disconnection",
      "glbValue": "DISCONNECTION",
      "description": "Request to disconnect service"
    },
    {
      "glbTypeValId": 1003,
      "name": "Maintenance",
      "glbValue": "MAINTENANCE",
      "description": "Maintenance request"
    }
  ]
}
```

### cURL Example
```bash
curl -X GET "http://localhost:8080/v1/dbs/api/account/servicerequest/types" \
  -H "Authorization: Bearer <token>"
```

---

## 7. Get Service Request Categories

Retrieves all available service request categories for dropdown selection.

### Endpoint
```http
GET /v1/dbs/api/account/servicerequest/category
```

### Response (200 OK)
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Success",
  "data": [
    {
      "glbTypeValId": 2001,
      "name": "Installation",
      "glbValue": "INSTALLATION",
      "description": "Installation related requests"
    },
    {
      "glbTypeValId": 2002,
      "name": "Billing",
      "glbValue": "BILLING",
      "description": "Billing related requests"
    }
  ]
}
```

### cURL Example
```bash
curl -X GET "http://localhost:8080/v1/dbs/api/account/servicerequest/category" \
  -H "Authorization: Bearer <token>"
```

---

## 8. Get Service Request Subcategories

Retrieves all available service request subcategories for dropdown selection.

### Endpoint
```http
GET /v1/dbs/api/account/servicerequest/subcategory
```

### Response (200 OK)
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Success",
  "data": [
    {
      "glbTypeValId": 3001,
      "name": "Residential Installation",
      "glbValue": "RES_INSTALLATION",
      "description": "Residential property installation"
    },
    {
      "glbTypeValId": 3002,
      "name": "Commercial Installation",
      "glbValue": "COM_INSTALLATION",
      "description": "Commercial property installation"
    }
  ]
}
```

### cURL Example
```bash
curl -X GET "http://localhost:8080/v1/dbs/api/account/servicerequest/subcategory" \
  -H "Authorization: Bearer <token>"
```

---

## 9. Submit Complete Service Request (Composite)

Submits a complete service request with all related data in a single transaction. This is the **recommended approach** for new service request submissions.

### Endpoint
```http
POST /v1/dbs/api/account/servicerequest/composite/submit-complete
```

### Benefits
- ✅ Reduces HTTP requests by 80-87%
- ✅ Single database transaction (ACID compliance)
- ✅ 70-85% reduction in processing time
- ✅ Eliminates risk of orphaned records
- ✅ Atomic rollback on any failure

### Request Body
```json
{
  "serviceRequest": {
    "accountId": 12345,
    "requestNumber": "SR-2024-002",
    "requestType": 1001,
    "requestCategory": 2001,
    "priority": 3001,
    "subject": "Complete Service Request with POS",
    "description": "Full service request with all related data",
    "dueDate": "2024-12-30"
  },
  "contacts": [
    {
      "contactType": "TECHNICAL",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "phone": "+628123456789",
      "isPrimary": true
    }
  ],
  "prerequisites": [
    {
      "prerequisiteType": "DOCUMENT",
      "name": "ID Card",
      "description": "National ID Card verification",
      "status": "COMPLETED"
    }
  ],
  "pointOfSale": {
    "saleDate": "2024-11-22",
    "totalAmount": 5000000,
    "products": [
      {
        "productId": 101,
        "quantity": 1,
        "unitPrice": 5000000
      }
    ]
  },
  "installment": {
    "numberOfPayments": 12,
    "paymentFrequency": "MONTHLY",
    "firstPaymentDate": "2024-12-01"
  },
  "attachments": [
    {
      "fileName": "id-card.pdf",
      "fileCategory": "DOCUMENT",
      "type": "PDF",
      "description": "ID Card copy",
      "base64Content": "JVBERi0xLjQK...",
      "fileSize": 102400
    }
  ],
  "submitForApproval": true,
  "approvalHierarchyId": 1
}
```

### Response (201 Created)
```json
{
  "success": true,
  "httpCode": 201,
  "message": "Service request submitted successfully",
  "data": {
    "serviceRequestId": 9877,
    "createdContactIds": [501, 502],
    "linkedPrerequisiteIds": [301, 302],
    "pointOfSaleId": 701,
    "installmentId": 801,
    "attachmentResults": [
      {
        "attachmentId": 901,
        "fileName": "id-card.pdf",
        "success": true
      }
    ],
    "approvalId": 1001,
    "approvalStatus": "PENDING",
    "overallStatus": "SUCCESS",
    "errors": []
  }
}
```

### cURL Example
```bash
curl -X POST "http://localhost:8080/v1/dbs/api/account/servicerequest/composite/submit-complete" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d @complete-request.json
```

---

# Account Relationship APIs

## Endpoints Overview

### Base Path: `/v1/dbs/api/accounts/{accountId}/relationships`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/{accountId}/relationships` | Get paginated list with filters |
| GET | `/{accountId}/relationships/{id}` | Get relationship details |
| POST | `/{accountId}/relationships/create` | Create new relationship |
| PUT | `/{accountId}/relationships/{id}` | Update existing relationship |
| DELETE | `/{accountId}/relationships/{id}` | Delete relationship |
| GET | `/{accountId}/relationship-types` | Get relationship types by category |

---

## 1. Get Relationship List (Paginated)

Retrieves a paginated, filtered list of relationships for an account.

### Endpoint
```http
POST /v1/dbs/api/accounts/{accountId}/relationships
```

### Path Parameters
- `accountId` (Integer): Account identifier

### Request Body
```json
{
  "search": "john",
  "filters": {
    "relationshipType": "FAMILY",
    "status": "ACTIVE"
  },
  "page": 0,
  "size": 10,
  "sort": "startDate,desc"
}
```

### Response (200 OK)
```json
{
  "page": {
    "size": 10,
    "totalElements": 15,
    "totalPages": 2,
    "number": 0
  },
  "content": [
    {
      "id": 1001,
      "accountId": 12345,
      "directionalFlag": "Y",
      "relationshipType": "FAMILY",
      "relationshipCategory": "PARTY",
      "objectId": 5001,
      "objectName": "John Doe",
      "objectValue": "SPOUSE",
      "startDate": "2024-01-01",
      "endDate": null,
      "description": "Spouse relationship",
      "status": "ACTIVE"
    }
  ]
}
```

### cURL Example
```bash
curl -X POST "http://localhost:8080/v1/dbs/api/accounts/12345/relationships" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "search": "",
    "page": 0,
    "size": 10
  }'
```

---

## 2. Get Relationship Detail

Retrieves detailed information about a specific relationship.

### Endpoint
```http
GET /v1/dbs/api/accounts/{accountId}/relationships/{id}
```

### Path Parameters
- `accountId` (Integer): Account identifier
- `id` (Integer): Relationship identifier

### Response (200 OK)
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Success",
  "data": {
    "id": 1001,
    "accountId": 12345,
    "directionalFlag": "Y",
    "relationshipType": "FAMILY",
    "relationshipCategory": "PARTY",
    "objectId": 5001,
    "objectName": "John Doe",
    "objectValue": "SPOUSE",
    "startDate": "2024-01-01T00:00:00.000+00:00",
    "endDate": null,
    "description": "Spouse relationship",
    "status": "ACTIVE",
    "createdBy": "admin",
    "createdDate": "2024-11-22T06:00:00.000+00:00"
  }
}
```

### cURL Example
```bash
curl -X GET "http://localhost:8080/v1/dbs/api/accounts/12345/relationships/1001" \
  -H "Authorization: Bearer <token>"
```

---

## 3. Create Relationship

Creates a new relationship for an account.

### Endpoint
```http
POST /v1/dbs/api/accounts/{accountId}/relationships/create
```

### Path Parameters
- `accountId` (Integer): Account identifier

### Request Body
```json
{
  "directionalFlag": "Y",
  "relationshipType": "FAMILY",
  "relationshipCategory": "PARTY",
  "objectId": 5001,
  "objectName": "Jane Smith",
  "objectValue": "DAUGHTER",
  "startDate": "2024-11-22",
  "endDate": null,
  "description": "Daughter relationship"
}
```

### Required Fields
- `relationshipType` (String): Type of relationship
- `relationshipCategory` (String): Category (PARTY or ACCOUNT)
- `objectId` (Integer): Related party/account ID
- `startDate` (Date): Relationship start date

### Optional Fields
- `directionalFlag` (String): Directional indicator (Y/N)
- `objectName` (String): Name of related object
- `objectValue` (String): Relationship value/role
- `endDate` (Date): Relationship end date
- `description` (String): Additional description

### Response (201 Created)
```json
{
  "success": true,
  "httpCode": 201,
  "message": "Relationship created successfully",
  "data": {
    "id": 1002,
    "accountId": 12345,
    "relationshipType": "FAMILY",
    "relationshipCategory": "PARTY",
    "objectId": 5001,
    "objectName": "Jane Smith",
    "objectValue": "DAUGHTER",
    "startDate": "2024-11-22T00:00:00.000+00:00",
    "status": "ACTIVE",
    "createdBy": "admin",
    "createdDate": "2024-11-22T09:00:00.000+00:00"
  }
}
```

### cURL Example
```bash
curl -X POST "http://localhost:8080/v1/dbs/api/accounts/12345/relationships/create" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "relationshipType": "FAMILY",
    "relationshipCategory": "PARTY",
    "objectId": 5001,
    "objectName": "Jane Smith",
    "objectValue": "DAUGHTER",
    "startDate": "2024-11-22"
  }'
```

---

## 4. Update Relationship

Updates an existing relationship.

### Endpoint
```http
PUT /v1/dbs/api/accounts/{accountId}/relationships/{id}
```

### Path Parameters
- `accountId` (Integer): Account identifier
- `id` (Integer): Relationship identifier

### Request Body
```json
{
  "objectValue": "PRIMARY_CONTACT",
  "description": "Updated to primary contact",
  "endDate": "2025-12-31"
}
```

### Response (200 OK)
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Relationship updated successfully",
  "data": {
    "id": 1002,
    "objectValue": "PRIMARY_CONTACT",
    "description": "Updated to primary contact",
    "endDate": "2025-12-31T00:00:00.000+00:00",
    "updatedBy": "admin",
    "updatedDate": "2024-11-22T10:00:00.000+00:00"
  }
}
```

### cURL Example
```bash
curl -X PUT "http://localhost:8080/v1/dbs/api/accounts/12345/relationships/1002" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "objectValue": "PRIMARY_CONTACT",
    "description": "Updated to primary contact"
  }'
```

---

## 5. Delete Relationship

Deletes (soft delete) a relationship.

### Endpoint
```http
DELETE /v1/dbs/api/accounts/{accountId}/relationships/{id}
```

### Path Parameters
- `accountId` (Integer): Account identifier
- `id` (Integer): Relationship identifier

### Response (200 OK)
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Relationship deleted successfully",
  "data": {
    "id": 1002,
    "status": "DELETED",
    "deletedBy": "admin",
    "deletedDate": "2024-11-22T11:00:00.000+00:00"
  }
}
```

### cURL Example
```bash
curl -X DELETE "http://localhost:8080/v1/dbs/api/accounts/12345/relationships/1002" \
  -H "Authorization: Bearer <token>"
```

---

## 6. Get Relationship Types

Retrieves available relationship types filtered by category.

### Endpoint
```http
GET /v1/dbs/api/accounts/{accountId}/relationship-types?category=PARTY
```

### Path Parameters
- `accountId` (Integer): Account identifier

### Query Parameters
- `category` (String, default: "ALL"): Filter by category
  - `PARTY`: Party-based relationships (8 types)
  - `ACCOUNT`: Account-based relationships (3 types)
  - `ALL`: All relationship types (11 types)

### Response (200 OK) - Category: PARTY
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Success",
  "data": [
    {
      "glbTypeValId": 2600,
      "name": "Guardian",
      "glbValue": "GUARDIAN",
      "category": "PARTY"
    },
    {
      "glbTypeValId": 2601,
      "name": "Spouse",
      "glbValue": "SPOUSE",
      "category": "PARTY"
    },
    {
      "glbTypeValId": 2602,
      "name": "Child",
      "glbValue": "CHILD",
      "category": "PARTY"
    },
    {
      "glbTypeValId": 2603,
      "name": "Parent",
      "glbValue": "PARENT",
      "category": "PARTY"
    },
    {
      "glbTypeValId": 2604,
      "name": "Sibling",
      "glbValue": "SIBLING",
      "category": "PARTY"
    },
    {
      "glbTypeValId": 2607,
      "name": "Emergency Contact",
      "glbValue": "EMERGENCY_CONTACT",
      "category": "PARTY"
    },
    {
      "glbTypeValId": 2608,
      "name": "Authorized Representative",
      "glbValue": "AUTHORIZED_REP",
      "category": "PARTY"
    },
    {
      "glbTypeValId": 2610,
      "name": "Business Partner",
      "glbValue": "BUSINESS_PARTNER",
      "category": "PARTY"
    }
  ]
}
```

### Response (200 OK) - Category: ACCOUNT
```json
{
  "success": true,
  "httpCode": 200,
  "message": "Success",
  "data": [
    {
      "glbTypeValId": 2605,
      "name": "Billing Account",
      "glbValue": "BILLING_ACCOUNT",
      "category": "ACCOUNT"
    },
    {
      "glbTypeValId": 2606,
      "name": "Service Account",
      "glbValue": "SERVICE_ACCOUNT",
      "category": "ACCOUNT"
    },
    {
      "glbTypeValId": 2609,
      "name": "Sub Account",
      "glbValue": "SUB_ACCOUNT",
      "category": "ACCOUNT"
    }
  ]
}
```

### cURL Examples
```bash
# Get PARTY relationships
curl -X GET "http://localhost:8080/v1/dbs/api/accounts/12345/relationship-types?category=PARTY" \
  -H "Authorization: Bearer <token>"

# Get ACCOUNT relationships
curl -X GET "http://localhost:8080/v1/dbs/api/accounts/12345/relationship-types?category=ACCOUNT" \
  -H "Authorization: Bearer <token>"

# Get ALL relationships
curl -X GET "http://localhost:8080/v1/dbs/api/accounts/12345/relationship-types?category=ALL" \
  -H "Authorization: Bearer <token>"
```

---

# Testing Guide

## Prerequisites

1. **Authentication Token**
   - Obtain JWT token from authentication endpoint
   - Set token in environment variable: `export TOKEN="your-jwt-token"`

2. **Test Account**
   - Create or identify a test account ID
   - Set account ID: `export ACCOUNT_ID=12345`

3. **API Base URL**
   - Set base URL: `export BASE_URL="http://localhost:8080"`

## Service Request Testing

### Test Scenario 1: Basic CRUD Operations

```bash
# 1. Get dropdown values
curl -X GET "$BASE_URL/v1/dbs/api/account/servicerequest/types" \
  -H "Authorization: Bearer $TOKEN"

curl -X GET "$BASE_URL/v1/dbs/api/account/servicerequest/category" \
  -H "Authorization: Bearer $TOKEN"

# 2. Create service request
SR_ID=$(curl -X POST "$BASE_URL/v1/dbs/api/account/servicerequest/create" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": '"$ACCOUNT_ID"',
    "requestNumber": "SR-TEST-001",
    "requestType": 1001,
    "requestCategory": 2001,
    "priority": 3001,
    "subject": "Test Service Request"
  }' | jq -r '.data.serviceRequestId')

echo "Created Service Request ID: $SR_ID"

# 3. Get service request detail
curl -X GET "$BASE_URL/v1/dbs/api/account/servicerequest/detail/$SR_ID" \
  -H "Authorization: Bearer $TOKEN"

# 4. Update service request
curl -X PUT "$BASE_URL/v1/dbs/api/account/servicerequest/update" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "serviceRequestId": '"$SR_ID"',
    "subject": "Updated Test Service Request",
    "priority": 3002
  }'

# 5. Get list of service requests
curl -X GET "$BASE_URL/v1/dbs/api/account/servicerequest/list/$ACCOUNT_ID?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"

# 6. Toggle status
curl -X PUT "$BASE_URL/v1/dbs/api/account/servicerequest/toggle/$SR_ID" \
  -H "Authorization: Bearer $TOKEN"
```

### Test Scenario 2: Composite Submission

```bash
# Submit complete service request
curl -X POST "$BASE_URL/v1/dbs/api/account/servicerequest/composite/submit-complete" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "serviceRequest": {
      "accountId": '"$ACCOUNT_ID"',
      "requestNumber": "SR-COMP-001",
      "requestType": 1001,
      "requestCategory": 2001,
      "priority": 3001,
      "subject": "Complete Test Request",
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
    "submitForApproval": false
  }'
```

## Relationship Testing

### Test Scenario 1: Relationship CRUD

```bash
# 1. Get relationship types
curl -X GET "$BASE_URL/v1/dbs/api/accounts/$ACCOUNT_ID/relationship-types?category=PARTY" \
  -H "Authorization: Bearer $TOKEN"

# 2. Create relationship
REL_ID=$(curl -X POST "$BASE_URL/v1/dbs/api/accounts/$ACCOUNT_ID/relationships/create" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "relationshipType": "FAMILY",
    "relationshipCategory": "PARTY",
    "objectId": 5001,
    "objectName": "Test Person",
    "objectValue": "SPOUSE",
    "startDate": "2024-11-22"
  }' | jq -r '.data.id')

echo "Created Relationship ID: $REL_ID"

# 3. Get relationship detail
curl -X GET "$BASE_URL/v1/dbs/api/accounts/$ACCOUNT_ID/relationships/$REL_ID" \
  -H "Authorization: Bearer $TOKEN"

# 4. Get relationship list
curl -X POST "$BASE_URL/v1/dbs/api/accounts/$ACCOUNT_ID/relationships" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "page": 0,
    "size": 10
  }'

# 5. Update relationship
curl -X PUT "$BASE_URL/v1/dbs/api/accounts/$ACCOUNT_ID/relationships/$REL_ID" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "objectValue": "PRIMARY_CONTACT",
    "description": "Updated relationship"
  }'

# 6. Delete relationship
curl -X DELETE "$BASE_URL/v1/dbs/api/accounts/$ACCOUNT_ID/relationships/$REL_ID" \
  -H "Authorization: Bearer $TOKEN"
```

## Postman Collection

Import the following collection into Postman for easier testing:

### Collection Structure
```
Account Management API
├── Service Requests
│   ├── Get Types
│   ├── Get Categories
│   ├── Get Subcategories
│   ├── Create Service Request
│   ├── Update Service Request
│   ├── Get Detail
│   ├── Get List
│   ├── Toggle Status
│   └── Composite
│       ├── Submit Complete
│       ├── Validate
│       ├── Save Draft
│       ├── Get Status
│       └── Resume
└── Relationships
    ├── Get Types (PARTY)
    ├── Get Types (ACCOUNT)
    ├── Get Types (ALL)
    ├── Create Relationship
    ├── Get Detail
    ├── Get List
    ├── Update Relationship
    └── Delete Relationship
```

### Environment Variables
```json
{
  "BASE_URL": "http://localhost:8080",
  "TOKEN": "your-jwt-token",
  "ACCOUNT_ID": "12345"
}
```

---

# Error Handling

## Standard Error Response Format

All API errors follow this consistent structure:

```json
{
  "success": false,
  "httpCode": 400,
  "message": "Error description",
  "data": null,
  "errors": [
    {
      "field": "requestNumber",
      "message": "Request number already exists"
    }
  ]
}
```

## Common HTTP Status Codes

| Code | Description | Common Causes |
|------|-------------|---------------|
| 200 | OK | Successful request |
| 201 | Created | Resource created successfully |
| 400 | Bad Request | Validation errors, invalid data |
| 401 | Unauthorized | Missing or invalid authentication token |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource does not exist |
| 409 | Conflict | Duplicate data (e.g., request number exists) |
| 500 | Internal Server Error | Server-side error |

## Validation Errors

### Service Request Validation

```json
{
  "success": false,
  "httpCode": 400,
  "message": "Validation failed",
  "data": [
    {
      "accountId": "Account ID cannot be null"
    },
    {
      "requestNumber": "Request number cannot be empty"
    },
    {
      "subject": "Subject cannot be empty"
    }
  ]
}
```

### Relationship Validation

```json
{
  "success": false,
  "httpCode": 400,
  "message": "Validation failed",
  "data": [
    {
      "relationshipType": "Relationship type cannot be null"
    },
    {
      "startDate": "Start date cannot be null"
    }
  ]
}
```

## Business Logic Errors

### Duplicate Request Number
```json
{
  "success": false,
  "httpCode": 400,
  "message": "Request number already exists",
  "data": null
}
```

### Resource Not Found
```json
{
  "success": false,
  "httpCode": 404,
  "message": "Service request not found",
  "data": null
}
```

### Composite Submission Failure
```json
{
  "success": false,
  "httpCode": 500,
  "message": "Failed to submit service request",
  "data": {
    "serviceRequestId": 9877,
    "errors": [
      {
        "step": "ATTACHMENT_UPLOAD",
        "error": "Failed to upload attachment: file-1.pdf",
        "details": "File size exceeds maximum limit"
      }
    ],
    "rollbackStatus": "SUCCESS"
  }
}
```

## Best Practices

1. **Always check the `success` field** before processing the response
2. **Handle HTTP status codes** appropriately in your client
3. **Log error responses** for debugging and monitoring
4. **Implement retry logic** for 500-level errors
5. **Validate data** on the client side before submission
6. **Use composite endpoints** for multi-step operations to ensure atomicity

---

## Support

For additional support or questions:
- **API Issues**: Contact backend team
- **Authentication**: Contact security team
- **Business Logic**: Contact product team

**Document Version**: 1.0
**Last Updated**: 2024-11-22
**Maintained By**: Backend Development Team
