package com.dbs.common.base.utils;

import com.dbs.common.base.entities.SearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.criteria.Path;

import static org.springframework.data.jpa.domain.Specification.where;

public class PagingUtils {

    private static final Logger logger = LoggerFactory.getLogger(PagingUtils.class);

    private PagingUtils() {
    }

    public static Pageable getPaging(MaterialTablePagingRequest pagingdata) {
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingdata.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;

                Order order;
                order = new Order(dire, sorts[0]).ignoreCase();
                orders.add(order);
            }
            int pageIndex = Math.max(pagingdata.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingdata.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingdata.getPage() - 1, pagingdata.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }

    @SuppressWarnings("java:S3740")
    public static GenericSpesification getSpecification(GenericSpesification genericSpesification, MaterialTablePagingRequest pagingdata) {
        try {
            //columnName~keyword
            for (String sr : pagingdata.getSearch()) {
                String[] searchs = sr.trim().split("~");
                genericSpesification.add(new SearchCriteria(searchs[0], searchs[1], SearchOperation.MATCH));
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
        }
        return genericSpesification;
    }

    @SuppressWarnings("java:S1452")
    public static Specification<?> createSpecificationSdEd(String input){
        String[] searchs = input.trim().split("~");
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get(searchs[0]).as(String.class)),
                        "%" + StringUtils.upperCase(searchs[1]) + "%");

    }

    @SuppressWarnings("java:S1452")
    public static Specification<?> createSpecification(String input, String selector) {
        String[] searchs = input.trim().split("~");
        selector = determineSelector(searchs, selector);

            switch (selector) {
            case Constant.EQUALS_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(searchs[0]),
                                castToRequiredType(root.get(searchs[0]).getJavaType(),
                                        searchs[1].toUpperCase()));

            case Constant.NOT_EQUALS_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(searchs[0]),
                                castToRequiredType(root.get(searchs[0]).getJavaType(),
                                        searchs[1]));

            case Constant.GREATER_THAN_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(searchs[0]),
                                (Number) castToRequiredType(
                                        root.get(searchs[0]).getJavaType(),
                                        searchs[1]));

            case Constant.LESS_THAN_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(searchs[0]),
                                (Number) castToRequiredType(
                                        root.get(searchs[0]).getJavaType(),
                                        searchs[1]));

            case Constant.DEFAULT_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.upper(root.get(searchs[0]).as(String.class)),
                                "%" + StringUtils.upperCase(searchs[1]) + "%");

            case Constant.BETWEEN_SELECTOR:
                return createBetweenSpecification(searchs);
            default:
                throw new EmptyStackException(Constant.OPERATION_NOT_SUPPORTED);
        }
    }

    @SuppressWarnings("java:S1452")
    public static Specification<?> createSpecificationRbi(String input, String selector) {
        String[] searchs = input.trim().split("~");
        if (searchs.length > 0 && (searchs[0].equals(Constant.START_DATE) || searchs[0].equals(Constant.END_DATE))) {
            selector = Constant.BETWEEN_SELECTOR;
        }
        if (searchs.length > 0 && (searchs[0].equals("sex") || searchs[0].equals("isDeleted"))) {
            selector = Constant.EQUALS_SELECTOR;
        }
        if (searchs.length > 0 && (searchs[0].equals("generateDate")
                || searchs[0].equals("createdDate")
                || searchs[0].equals("invoiceDate")
                || searchs[0].equals("proformaInvoiceDate")
                || searchs[0].equals("transactionDate")
                || searchs[0].equals("dueDate")
                || searchs[0].equals("rateDate")
                || searchs[0].equals("accountingDate")
                || searchs[0].equals("calculateAt"))) {
            selector = Constant.BETWEEN_SELECTOR;
        }
        if (searchs.length > 0 && (searchs[0].equals(Constant.STATUS))) {
            selector = Constant.EQUALS_SELECTOR;
        }

        switch (selector) {
            case Constant.EQUALS_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(searchs[0]),
                                castToRequiredType(root.get(searchs[0]).getJavaType(),
                                        searchs[1].toUpperCase()));

            case Constant.NOT_EQUALS_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(searchs[0]),
                                castToRequiredType(root.get(searchs[0]).getJavaType(),
                                        searchs[1]));

            case Constant.GREATER_THAN_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(searchs[0]),
                                (Number) castToRequiredType(
                                        root.get(searchs[0]).getJavaType(),
                                        searchs[1]));

            case Constant.LESS_THAN_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(searchs[0]),
                                (Number) castToRequiredType(
                                        root.get(searchs[0]).getJavaType(),
                                        searchs[1]));

            case Constant.DEFAULT_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.upper(root.get(searchs[0]).as(String.class)),
                                "%" + StringUtils.upperCase(searchs[1]) + "%");

            case Constant.BETWEEN_SELECTOR:
                return (Specification<Object>) (root, query, criteriaBuilder) -> {
                    Path<Date> dateEntryPath = root.get(searchs[0]);
                    Date endDate = stringToDate(searchs[1] + Constant.VALUE_END_TIME, Constant.FORMAT_DATETIME);
                    return criteriaBuilder.between(dateEntryPath,
                            stringToDate(searchs[1], Constant.FORMAT_DATE), endDate);
                };
            default:
                throw new EmptyStackException(Constant.OPERATION_NOT_SUPPORTED);
        }
    }

    @SuppressWarnings("java:S1452")
    public static Specification<?> createSpecification(String column, String value, String selector) {
        switch (selector) {
            case Constant.EQUALS_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(column),
                                castToRequiredType(root.get(column).getJavaType(),
                                        value));

            case Constant.NOT_EQUALS_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(column),
                                castToRequiredType(root.get(column).getJavaType(),
                                        value));

            case Constant.GREATER_THAN_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(column),
                                (Number) castToRequiredType(
                                        root.get(column).getJavaType(),
                                        value));

            case Constant.LESS_THAN_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(column),
                                (Number) castToRequiredType(
                                        root.get(column).getJavaType(),
                                        value));

            case Constant.DEFAULT_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.upper(root.get(column).as(String.class)),
                                "%" + StringUtils.upperCase(value) + "%");

            case Constant.BETWEEN_SELECTOR:
                return (Specification<Object>) (root, query, criteriaBuilder) -> {
                    Path<Date> dateEntryPath = root.get(column);
                    Date endDate = stringToDate(value + Constant.VALUE_END_TIME, Constant.FORMAT_DATETIME);
                    return criteriaBuilder.between(dateEntryPath,
                            stringToDate(value, Constant.FORMAT_DATE), endDate);
                };

            case Constant.IS_NULL_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.isNull(root.get(column));
            default:
                throw new EmptyStackException(Constant.OPERATION_NOT_SUPPORTED);
        }
    }

    @SuppressWarnings("java:S1452")
    public static Specification<?> createSpecificationPayment(String input, String selector) {
        String[] searchs = input.trim().split("~");
        selector = determineSelector(searchs, selector);

        if (searchs.length > 0 && (searchs[0].equals(Constant.IS_MISC) || searchs[0].equals(Constant.IS_RECONCILED)) && StringUtils.isNotBlank(searchs[1]))
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(
                            root.get(searchs[0]),
                            castToRequiredType(root.get(searchs[0]).getJavaType(), searchs[1].equalsIgnoreCase(Boolean.TRUE.toString()) ? "Y" : "N")
                    );

        switch (selector) {
            case Constant.EQUALS_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(searchs[0]),
                                castToRequiredType(root.get(searchs[0]).getJavaType(),
                                        searchs[0].equals(Constant.STATUS) ? CommonHelper.capitalizeFully(searchs[1]) : searchs[1]));

            case Constant.NOT_EQUALS_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(searchs[0]),
                                castToRequiredType(root.get(searchs[0]).getJavaType(),
                                        searchs[1]));

            case Constant.GREATER_THAN_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(searchs[0]),
                                (Number) castToRequiredType(
                                        root.get(searchs[0]).getJavaType(),
                                        searchs[1]));

            case Constant.LESS_THAN_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(searchs[0]),
                                (Number) castToRequiredType(
                                        root.get(searchs[0]).getJavaType(),
                                        searchs[1]));

            case Constant.DEFAULT_SELECTOR:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.upper(root.get(searchs[0]).as(String.class)),
                                "%" + StringUtils.upperCase(searchs[1]) + "%");

            case Constant.BETWEEN_SELECTOR:
                return createBetweenSpecification(searchs);
            default:
                throw new EmptyStackException(Constant.OPERATION_NOT_SUPPORTED);
        }
    }

    @SuppressWarnings("java:S1452")
    public static <T> Specification<?> createINSpecification(String column, List<T> input) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(column))
                        .value(input);
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createEntityFilter(Specification spec, Integer entityId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("entityId~" + entityId, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("entityId~" + entityId, Constant.EQUALS_SELECTOR));
    }

    public static Specification<?> createAccountIdFilter(Specification spec, Integer accountId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("accountId~" + accountId, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("accountId~" + accountId, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCalTypeFilter(Specification spec, Integer calType, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("calType~" + calType, Constant.DEFAULT_SELECTOR)) : spec.and(PagingUtils.createSpecification("calType~" + calType, Constant.DEFAULT_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCalCodeFilter(Specification spec, String calCode, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("calCode~" + calCode, Constant.DEFAULT_SELECTOR)) : spec.and(PagingUtils.createSpecification("calCode~" + calCode, Constant.DEFAULT_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createBatchIdFilter(Specification spec, Integer batchId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("batchId~" + batchId, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("batchId~" + batchId, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createProductIdFilter(Specification spec, Integer productId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("productId~" + productId, Constant.DEFAULT_SELECTOR)) : spec.and(PagingUtils.createSpecification("productId~" + productId, Constant.DEFAULT_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createHeaderFilter(Specification spec, String header, Integer headerId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification(header + "~" + headerId, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification(header + "~" + headerId, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createHeaderFilterString(Specification spec, String header, String headerId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification(header + "~" + headerId, DEFAULT_SELECTOR)) : spec.and(PagingUtils.createSpecification(header + "~" + headerId, DEFAULT_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createIsDeletedFilter(Specification spec, Boolean isDeleted, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("isDeleted~" + isDeleted, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("isDeleted~" + isDeleted, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createStatusFilter(Specification spec, String status, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("status"+"~" + status, DEFAULT_SELECTOR)) : spec.and(PagingUtils.createSpecification("status"+"~" + status, DEFAULT_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCostCenterFilter(Specification spec, List<Integer> costCenterList, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createINSpecification("ccId", costCenterList)) : spec.and(PagingUtils.createINSpecification("ccId", costCenterList));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCostCenterFilterForCostCenterIdColumnName(Specification spec, List<Integer> costCenterList, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createINSpecification("costCenterId", costCenterList)) : spec.and(PagingUtils.createINSpecification("costCenterId", costCenterList));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createAccountCostCenterFilterForCostCenterIdColumnName(Specification spec, List<Integer> costCenterList, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createINSpecification("accountCostCenterId", costCenterList)) : spec.and(PagingUtils.createINSpecification("accountCostCenterId", costCenterList));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCostCenterFilterForVwCusInfoCc(Specification spec, List<Integer> costCenterList, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createINSpecification("ccId", costCenterList)) : spec.and(PagingUtils.createINSpecification("ccId", costCenterList));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createAccountGroupFilter(Specification spec, String accountGroup, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("accountGroup~" + accountGroup, Constant.DEFAULT_SELECTOR)) : spec.and(PagingUtils.createSpecification("accountGroup~" + accountGroup, Constant.DEFAULT_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createRefIdInFilter(Specification spec, List<Integer> refIds, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createINSpecification("referenceId", refIds)) : spec.and(PagingUtils.createINSpecification("referenceId", refIds));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCustomerFilter(Specification spec, String customerId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("customerId~" + customerId, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("customerId~" + customerId, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createIdFilter(Specification spec, List<Integer> id, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createINSpecification("id", id)) : spec.and(PagingUtils.createINSpecification("id", id));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createAppHierIdFilter(Specification spec, Integer apphierId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("apphierId~" + apphierId, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("apphierId~" + apphierId, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings("java:S3740")
    public static Object castToRequiredType(Class fieldType, String value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (fieldType.isAssignableFrom(Boolean.class)) {
            return Boolean.valueOf(value);
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            return Enum.valueOf(fieldType, value);
        }
        return value;
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createReceiptIdFilter(Specification spec, Long receiptId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("receiptId~" + receiptId, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("receiptId~" + receiptId, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createBankStatementIdFilter(Specification spec, Long bankStatementId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("bankStatementId~" + bankStatementId, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("bankStatementId~" + bankStatementId, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createStatusReconcileFilter(Specification spec, String statusReconcile, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("statusReconcile~" + statusReconcile, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("statusReconcile~" + statusReconcile, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createStatusApprovalFilter(Specification spec, String statusApproval, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("statusApproval", statusApproval, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("statusApproval", statusApproval, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createStatusFilterV1(Specification spec, String status, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("status~" + status, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("status~" + status, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createPaymentItemIdFilter(Specification spec, Long paymentItemId, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification("paymentItemId~" + paymentItemId, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification("paymentItemId~" + paymentItemId, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCommonColumnLikeFilter(Specification spec, String column, String value, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification(column.concat("~") + value, DEFAULT_SELECTOR)) : spec.and(PagingUtils.createSpecification(column.concat("~") + value, DEFAULT_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCommonColumnNumberEqualsFilter(Specification spec, String column, Long value, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification(column.concat("~") + value, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification(column.concat("~") + value, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCommonColumnVarcharEqualsFilter(Specification spec, String column, String value, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification(column.concat("~") + value, Constant.EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification(column.concat("~") + value, Constant.EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCommonColumnVarcharNotEqualsFilter(Specification spec, String column, String value, Boolean isFirst) {
        return isFirst.equals(Boolean.TRUE) ? where(PagingUtils.createSpecification(column.concat("~") + value, Constant.NOT_EQUALS_SELECTOR)) : spec.and(PagingUtils.createSpecification(column.concat("~") + value, Constant.NOT_EQUALS_SELECTOR));
    }

    @SuppressWarnings({"java:S1452", "java:S3740"})
    public static Specification<?> createCommonColumnIsNullFilter(Specification spec, String column, Boolean isFirst) {
        return Boolean.TRUE.equals(isFirst) ? where(PagingUtils.createSpecification(column, null, Constant.IS_NULL_SELECTOR)) : spec.and(PagingUtils.createSpecification(column, null, Constant.IS_NULL_SELECTOR));
    }

    public static Date stringToDate(String dateString, String format) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        try {
            return (new SimpleDateFormat(format)).parse(dateString);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage());
            return null;
        }
    }

    public static Date toDate(final String date) {
        return toDate(date, "00:00.00.000");
    }

    public static Date toDate(final String date, final String time) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + time);
        } catch (ParseException e) {
            throw new EmptyStackException(e);
        }
    }

    public static Pageable getPagingTPayLateCharge(MaterialTablePagingRequest pagingRequest) {
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingRequest.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                boolean check = vwTPayLateChargeFieldSwitch(sorts[0].trim());
                String sort;
                if (check) {
                    sort = sorts[0]+Constant.REAL;
                } else {
                    sort = sorts[0];
                }
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Order order = new Order(dire, sort);
                orders.add(order);
            }
            int pageIndex = Math.max(pagingRequest.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingRequest.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingRequest.getPage() - 1, pagingRequest.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }

    public static Pageable getPagingReceipt(MaterialTablePagingRequest pagingRequest) {
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingRequest.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                boolean check = vwReceiptFieldSwitch(sorts[0].trim());
                String sort;
                if (check) {
                    sort = sorts[0]+Constant.REAL;
                } else {
                    sort = sorts[0];
                }
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Order order = new Order(dire, sort);
                orders.add(order);
            }
            int pageIndex = Math.max(pagingRequest.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingRequest.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingRequest.getPage() - 1, pagingRequest.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }
    
    public static Pageable getPagingPosRbi(MaterialTablePagingRequest pagingdata) {
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingdata.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                boolean check = posFieldSwitch(sorts[0].trim());
                String sort;
                if (check) {
                    sort = sorts[0]+Constant.REAL;
                } else {
                    sort = sorts[0];
                }
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Order order = new Order(dire, sort);
                orders.add(order);
            }
            int pageIndex = Math.max(pagingdata.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingdata.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingdata.getPage() - 1, pagingdata.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }
    
    public static Pageable getPagingBillingRbi(MaterialTablePagingRequest pagingdata) {
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingdata.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                boolean check = billingFieldSwitch(sorts[0].trim());
                String sort;
                if (check) {
                    sort = sorts[0]+Constant.REAL;
                } else {
                    sort = sorts[0];
                }
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Order order = new Order(dire, sort);
                orders.add(order);
            }
            int pageIndex = Math.max(pagingdata.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingdata.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingdata.getPage() - 1, pagingdata.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }

    public static Pageable getPagingInvoiceRbi(MaterialTablePagingRequest pagingdata) {
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingdata.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                boolean check = invoiceFieldSwitch(sorts[0].trim());
                String sort;
                if (check) {
                    sort = sorts[0]+Constant.REAL;
                } else {
                    sort = sorts[0];
                }
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Order order = new Order(dire, sort);
                orders.add(order);
            }
            int pageIndex = Math.max(pagingdata.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingdata.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingdata.getPage() - 1, pagingdata.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }

    public static Pageable getPagingBillingItemRbi(MaterialTablePagingRequest pagingdata) {
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingdata.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                boolean check = billingItemFieldSwitch(sorts[0].trim());
                String sort;
                if (check) {
                    sort = sorts[0]+Constant.REAL;
                } else {
                    sort = sorts[0];
                }
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Order order = new Order(dire, sort);
                orders.add(order);
            }
            int pageIndex = Math.max(pagingdata.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingdata.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingdata.getPage() - 1, pagingdata.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }

    public static Pageable getPagingAdjustmentBillingRbi(MaterialTablePagingRequest pagingdata) {
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingdata.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                boolean check = adjustmentBillingFieldSwitch(sorts[0].trim());
                String sort;
                if (check) {
                    sort = sorts[0]+Constant.REAL;
                } else {
                    sort = sorts[0];
                }
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Order order = new Order(dire, sort);
                orders.add(order);
            }
            int pageIndex = Math.max(pagingdata.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingdata.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingdata.getPage() - 1, pagingdata.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }

    public static Pageable getPagingRatingRbi(MaterialTablePagingRequest pagingdata) {
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingdata.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                boolean check = ratingFieldSwitch(sorts[0].trim());
                String sort;
                if (check) {
                    sort = sorts[0]+Constant.REAL;
                } else {
                    sort = sorts[0];
                }
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Order order = new Order(dire, sort);
                orders.add(order);
            }
            int pageIndex = Math.max(pagingdata.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingdata.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingdata.getPage() - 1, pagingdata.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }

    public static Pageable getPagingDailyRates(MaterialTablePagingRequest pagingdata) {
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingdata.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                boolean check = dailyRatesFieldSwitch(sorts[0].trim());
                String sort;
                if (check) {
                    sort = sorts[0]+Constant.REAL;
                } else {
                    sort = sorts[0];
                }
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Order order = new Order(dire, sort);
                orders.add(order);
            }
            int pageIndex = Math.max(pagingdata.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingdata.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingdata.getPage() - 1, pagingdata.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }

    public static Pageable getPagingUsage(MaterialTablePagingRequest pagingdata){
        List<Order> orders = new ArrayList<>();
        Pageable paging;
        try {
            for (String sortOrder : pagingdata.getSort()) {
                String[] sorts = sortOrder.trim().split("~");
                boolean check = usageFieldSwitch(sorts[0].trim());
                String sort;
                if (check) {
                    sort = sorts[0]+Constant.REAL;
                } else {
                    sort = sorts[0];
                }
                Sort.Direction dire = sorts[1].trim().toUpperCase().contains(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Order order = new Order(dire, sort);
                orders.add(order);
            }
            int pageIndex = Math.max(pagingdata.getPage() - 1, 0);
            paging = PageRequest.of(pageIndex, pagingdata.getSize(), Sort.by(orders));
        } catch (Exception e) {
            paging = PageRequest.of(pagingdata.getPage() - 1, pagingdata.getSize(), Sort.by(new Order(Sort.Direction.DESC, Constant.DEFAULT_SORT_BY)));
        }
        return paging;
    }

    private static boolean posFieldSwitch(String field) {
        boolean result;
        switch (field) {
            case Constant.AMOUNT:
                result = Boolean.TRUE;
                break;
            case "amountIdr":
                result = Boolean.TRUE;
                break;
            case "amountUsd":
                result = Boolean.TRUE;
                break;
            case Constant.DISCOUNT_AMOUNT:
                result = Boolean.TRUE;
                break;
            case "discountAmountIdr":
                result = Boolean.TRUE;
                break;
            case "discountAmountUsd":
                result = Boolean.TRUE;
                break;
            case "taxBasisEqvIdr":
                result = Boolean.TRUE;
                break;
            case "taxBasisEqvUsd":
                result = Boolean.TRUE;
                break;
            case "taxBasisIdr":
                result = Boolean.TRUE;
                break;
            case "taxBasisUsd":
                result = Boolean.TRUE;
                break;
            case "taxRate":
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_IDR:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_USD:
                result = Boolean.TRUE;
                break;
            case "vat":
                result = Boolean.TRUE;
                break;
            case "vatEqvIdr":
                result = Boolean.TRUE;
                break;
            case "vatIdr":
                result = Boolean.TRUE;
                break;
            case "vatUsd":
                result = Boolean.TRUE;
                break;
            case "withholdingTax":
                result = Boolean.TRUE;
                break;
            case "rate":
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_EQV_IDR:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_EQV_USD:
                result = Boolean.TRUE;
                break;
            default:
                result = Boolean.FALSE;
                break;
        }
        return result;
    }
    private static boolean billingFieldSwitch(String field) {
        boolean result;
        switch (field) {
            case "basicBillingIdr":
                result = Boolean.TRUE;
                break;
            case "basicBillingUsd":
                result = Boolean.TRUE;
                break;
            case "discountAmountIdr":
                result = Boolean.TRUE;
                break;
            case "discountAmountUsd":
                result = Boolean.TRUE;
                break;
            case "otherBillIdr":
                result = Boolean.TRUE;
                break;
            case "otherBillUsd":
                result = Boolean.TRUE;
                break;
            case "taxBasicEqvIdr":
                result = Boolean.TRUE;
                break;
            case "taxBasicUsd":
                result = Boolean.TRUE;
                break;
            case "taxBasicIdr":
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_EQV_IDR:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_EQV_USD:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_IDR:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_USD:
                result = Boolean.TRUE;
                break;
            case "totalBasicBillEqvIdr":
                result = Boolean.TRUE;
                break;
            case "totalBasicBillEqvUsd":
                result = Boolean.TRUE;
                break;
            case "totalOtherBillEqvIdr":
                result = Boolean.TRUE;
                break;
            case "totalOtherBillEqvUsd":
                result = Boolean.TRUE;
                break;
            case "vatEqvIdr":
                result = Boolean.TRUE;
                break;
            case "vatIdr":
                result = Boolean.TRUE;
                break;
            case "vatUsd":
                result = Boolean.TRUE;
                break;
            case "taxRate":
                result = Boolean.TRUE;
                break;
            case "rateReal":
                result = Boolean.TRUE;
                break;
            case "minContract":
                result = Boolean.TRUE;
                break;
            case "maxContract":
                result = Boolean.TRUE;
                break;
            case "totalUsage":
                result = Boolean.TRUE;
                break;
            case "totalUsageConvM3":
                result = Boolean.TRUE;
                break;
            case "totalUsageConvMmbtu":
                result = Boolean.TRUE;
                break;
            default:
                result = Boolean.FALSE;
                break;
        }
        return result;
    }

    private static boolean invoiceFieldSwitch(String field) {
        boolean result;
        switch (field) {
            case Constant.TOTAL_AMOUNT_IDR:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_USD:
                result = Boolean.TRUE;
                break;
            case Constant.taxBasicIdr:
                result = Boolean.TRUE;
                break;
            case Constant.taxBasicUsd:
                result = Boolean.TRUE;
                break;
            case "taxBasicEqvIdr":
                result = Boolean.TRUE;
                break;
            case "vatIdr":
                result = Boolean.TRUE;
                break;
            case "vatUsd":
                result = Boolean.TRUE;
                break;
            case "vatEqvIdr":
                result = Boolean.TRUE;
                break;
            case "withHoldingTax":
                result = Boolean.TRUE;
                break;
            case "taxRate":
                result = Boolean.TRUE;
                break;
            case "discountAmountIdr":
                result = Boolean.TRUE;
                break;
            case "discountAmountUsd":
                result = Boolean.TRUE;
                break;
            case "rate":
                result = Boolean.TRUE;
                break;
            case "totalAmountEqvIdr":
                result = Boolean.TRUE;
                break;
            case "totalAmountEqvUsd":
                result = Boolean.TRUE;
                break;
            case "amountIdr":
                result = Boolean.TRUE;
                break;
            case "amountUsd":
                result = Boolean.TRUE;
                break;
            case "minContract":
                result = Boolean.TRUE;
                break;
            case "maxContract":
                result = Boolean.TRUE;
                break;
            case "totalUsage":
                result = Boolean.TRUE;
                break; 
            case "totalUsageConvM3":
                result = Boolean.TRUE;
                break; 
            case "totalUsageConvMmbtu":
                result = Boolean.TRUE;
                break; 
             case "basicBillingIdr":
                result = Boolean.TRUE;
                break; 
            case "basicBillingUsd":
                result = Boolean.TRUE;
                break; 
            case "totalBasicBillEqvIdr":
                result = Boolean.TRUE;
                break;
            case "totalBasicBillEqvUsd":
                result = Boolean.TRUE;
                break;
            case "otherBillIdr":
                result = Boolean.TRUE;
                break;
            case "otherBillUsd":
                result = Boolean.TRUE;
                break;
            case "totalOtherBillEqvIdr":
                result = Boolean.TRUE;
                break;
            case "totalOtherBillEqvUsd":
                result = Boolean.TRUE;
                break;
            case "prevWithHoldingTax":
                result = Boolean.TRUE;
                break;
            default:
                result = Boolean.FALSE;
                break;
        }
        return result;
    }

    private static boolean billingItemFieldSwitch(String field) {
        boolean result;
        switch (field) {
            case "price":
                result = Boolean.TRUE;
                break;
            case Constant.AMOUNT:
                result = Boolean.TRUE;
                break;
            case Constant.DISCOUNT_AMOUNT:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_EQV_IDR:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_EQV_USD:
                result = Boolean.TRUE;
                break;
            case "rate":
                result = Boolean.TRUE;
                break;
            default:
                result = Boolean.FALSE;
                break;
        }
        return result;
    }
    private static boolean adjustmentBillingFieldSwitch(String field) {
        boolean result;
        switch (field) {
            case "totalAdjustmentAmount":
                result = Boolean.TRUE;
                break;
            case "totalAdjustmentAmountIdr":
                result = Boolean.TRUE;
                break;
            case "totalAdjustmentAmountUsd":
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_EQV_IDR:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_EQV_USD:
                result = Boolean.TRUE;
                break;
            case "rateType":
                result = Boolean.TRUE;
                break;
            default:
                result = Boolean.FALSE;
                break;
        }
        return result;
    }

    private static boolean ratingFieldSwitch(String field) {
        boolean result;
        switch (field) {
            case Constant.DISCOUNT_AMOUNT:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_EQV_IDR:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_EQV_USD:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_IDR:
                result = Boolean.TRUE;
                break;
            case Constant.TOTAL_AMOUNT_USD:
                result = Boolean.TRUE;
                break;
            case "amountEqvIdr":
                result = Boolean.TRUE;
                break;
            case "amountEqvUsd":
                result = Boolean.TRUE;
                break;
            case "amountIdr":
                result = Boolean.TRUE;
                break;
            case "amountUsd":
                result = Boolean.TRUE;
                break;
            case "rate":
                result = Boolean.TRUE;
                break;
            case "totalAmountMinEqvIdr":
                result = Boolean.TRUE;
                break;
            case "totalAmountMinEqvUsd":
                result = Boolean.TRUE;
                break;
            case "totalAmountMinIdr":
                result = Boolean.TRUE;
                break;
            case "totalAmountMinUsd":
                result = Boolean.TRUE;
                break;
            case "totalAmountNormalEqvIdr":
                result = Boolean.TRUE;
                break;
            case "totalAmountNormalEqvUsd":
                result = Boolean.TRUE;
                break;
            case "totalAmountNormalIdr":
                result = Boolean.TRUE;
                break;
            case "totalAmountNormalUsd":
                result = Boolean.TRUE;
                break;
            case "totalAmountOupEqvIdr":
                result = Boolean.TRUE;
                break;
            case "totalAmountOupEqvUsd":
                result = Boolean.TRUE;
                break;
            case "totalAmountOupIdr":
                result = Boolean.TRUE;
                break;
            case "totalAmountOupUsd":
                result = Boolean.TRUE;
                break;
            case "maxContract":
                result = Boolean.TRUE;
                break;
            case "minContract":
                result = Boolean.TRUE;
                break;
            case "convUsageM3":
                result = Boolean.TRUE;
                break;
            case "convUsageMmbtu":
                result = Boolean.TRUE;
                break;
            case "usage":
                result = Boolean.TRUE;
                break;
            case "totalUsage":
                result = Boolean.TRUE;
                break;
            case "convTotalUsageM3":
                result = Boolean.TRUE;
                break;
            case "convTotalUsageMmbtu":
                result = Boolean.TRUE;
                break;
            case "minimumUsage":
                result = Boolean.TRUE;
                break;
            case "convNormalUsageM3":
                result = Boolean.TRUE;
                break;
            case "normalUsage":
                result = Boolean.TRUE;
                break;
            case "convNormalUsageMmbtu":
                result = Boolean.TRUE;
                break;
            case "oup":
                result = Boolean.TRUE;
                break;
            case "calculatedUsage":
                result = Boolean.TRUE;
                break;
            case "convCalculatedUsageM3":
                result = Boolean.TRUE;
                break;
            case "convCalculatedUsageMmbtu":
                result = Boolean.TRUE;
                break;
            case "convMinimumUsageM3":
                result = Boolean.TRUE;
                break;
            case "convOupM3":
                result = Boolean.TRUE;
                break;
            case "convOupMmbtu":
                result = Boolean.TRUE;
                break;
            default:
                result = Boolean.FALSE;
                break;
        }
        return result;
    }

    private static boolean vwReceiptFieldSwitch(String field) {
        boolean result;
        switch (field) {
            case Constant.AMOUNT:
                result = Boolean.TRUE;
                break;
            case "rateAmount":
                result = Boolean.TRUE;
                break;
            case "equivalentAmount":
                result = Boolean.TRUE;
                break;
            case "appliedAmount":
                result = Boolean.TRUE;
                break;
            case "equivalentAppliedAmount":
                result = Boolean.TRUE;
                break;
            case "unAppliedAmount":
                result = Boolean.TRUE;
                break;
            case "equivalentUnAppliedAmount":
                result = Boolean.TRUE;
                break;
            case "refundAmount":
                result = Boolean.TRUE;
                break;
            case "transferAmount":
                result = Boolean.TRUE;
                break;
            default:
                result = Boolean.FALSE;
                break;
        }
        return result;
    }
    private static boolean dailyRatesFieldSwitch(String field) {
        boolean result;
        if (field.equalsIgnoreCase("convertedRate")) {
            result = Boolean.TRUE;
        } else {
            result = Boolean.FALSE;
        }
        return result;
    }

    private static boolean vwTPayLateChargeFieldSwitch(String field) {
        boolean result = Boolean.FALSE;
        if (field.equalsIgnoreCase(Constant.TOTAL_AMOUNT))
            result = Boolean.TRUE;

        return result;
    }

    private static boolean usageFieldSwitch(String field) {
        boolean result;
        switch (field) {
            case "engMeasured":
                result = Boolean.TRUE;
                break;
            case "calorie":
                result = Boolean.TRUE;
                break;
            case "beginStand":
                result = Boolean.TRUE;
                break;
            case "endStand":
                result = Boolean.TRUE;
                break;
            case "volMeasured27":
                result = Boolean.TRUE;
                break;
            case "volMeasured60":
                result = Boolean.TRUE;
                break;
             case "ghv":
                result = Boolean.TRUE;
                break;
            default:
                result = Boolean.FALSE;
                break;
        }
        return result;
    }

    private static String determineSelector(String[] searchs, String selector) {
        if (searchs.length > 0 && (searchs[0].equals(Constant.START_DATE) || searchs[0].equals(Constant.END_DATE) ||
                searchs[0].equals("rateDate") || searchs[0].equals("generateDate") || searchs[0].equals("createdDate") ||
                searchs[0].equals("transactionDate") || searchs[0].equals("accountDate") || 
                searchs[0].equals("documentDate") || searchs[0].equals("taxRateDate") || searchs[0].equals("actionDate") || searchs[0].equals("calDate") ||
                searchs[0].equals("dueDate") || searchs[0].equals("invoiceDate") ||
                searchs[0].equals("calculateAt") || searchs[0].equals(Constant.RECEIPT_DATE_KEY) || searchs[0].equals("fdate") || searchs[0].equals("effectiveDate") || searchs[0].equals("installDate") || searchs[0].equals("unInstallDate") || searchs[0].equals("saDate") || searchs[0].equals("gasInPlanDate") || searchs[0].equals("commitmentDate") || searchs[0].equals("appliedDate"))) {
            return Constant.BETWEEN_SELECTOR;
        }
        if (searchs.length > 0 && (searchs[0].equals("sex") || searchs[0].equals("isDeleted"))) {
            return Constant.EQUALS_SELECTOR;
        }
        if (searchs.length > 0 && (searchs[0].equals(Constant.STATUS) || searchs[0].equals("accountStatus"))) {
            return Constant.EQUALS_SELECTOR;
        }
        return selector;
    }

    private static Specification<Object> createBetweenSpecification(String[] searchs) {
        return (root, query, criteriaBuilder) -> {
            Path<Date> dateEntryPath = root.get(searchs[0]);
            var startDate = stringToDate(searchs[1], Constant.FORMAT_START_END_DATE);
            var endDate = stringToDate(searchs[1] + Constant.VALUE_END_TIME, Constant.FORMAT_DATETIME_VIEW);

            // filter datetime
            if (isDateTimeField(searchs[0], searchs[1])) {
                String dateValue = searchs[1];
                if (dateValue.endsWith("00:00:00")) {
                    // between 00:00:00 until 23:59:00
                    var lengthIndexDate = dateValue.length() - 9;
                    var date = dateValue.substring(0, lengthIndexDate);
                    startDate = stringToDate(date, Constant.FORMAT_START_END_DATE);
                    endDate = stringToDate(date + Constant.VALUE_END_TIME, Constant.FORMAT_DATETIME_VIEW);
                } else {
                    // exactly 15:27:28
                    startDate = stringToDate(dateValue + ".000", Constant.FORMAT_DATETIME_VIEW_WITH_MILLIS);
                    endDate = stringToDate(dateValue + ".999", Constant.FORMAT_DATETIME_VIEW_WITH_MILLIS);
                }
            }

            return criteriaBuilder.between(dateEntryPath, startDate, endDate);
        };
    }

    private static boolean isDateTimeField(String field, String dateValue) {
        if (field.equals(Constant.RECEIPT_DATE_KEY) && !ObjectUtils.isEmpty(stringToDate(dateValue, Constant.FORMAT_START_END_DATE))) {
            return false;
        }

        return field.equals("uploadDate") || field.equals(Constant.RECEIPT_DATE_KEY) || field.equals("actionDate") || field.equals("calDate");
    }
}