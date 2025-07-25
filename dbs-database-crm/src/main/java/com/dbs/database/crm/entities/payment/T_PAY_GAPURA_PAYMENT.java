package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "T_PAY_GAPURA_PAYMENT")
@Data
public class T_PAY_GAPURA_PAYMENT extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = 6189616229608038186L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_PAY_GAPURA_PAYMENT_SEQ")
    @Column(name = "ID", nullable = false, insertable = false)
    @SequenceGenerator(sequenceName = "T_PAY_GAPURA_PAYMENT_SEQ", allocationSize = 1, name = "T_PAY_GAPURA_PAYMENT_SEQ")
    private Long id;

    @Column(name = "NO_PELANGGAN")
    private String noPelanggan;

    @Column(name = "NAMA_PELANGGAN")
    private String namaPelanggan;

    @Column(name = "NILAI_TAGIHAN")
    private Integer nilaiTagihan;

    @Column(name = "VOLUME_PEMAKAIAN")
    private Integer volumePemakaian;

    @Column(name = "JENIS_PELANGGAN")
    private String jenisPelanggan;

    @Column(name = "JUMLAH_BULAN_TAGIHAN")
    private Integer jumlahBulanTagihan;

    @Column(name = "BULAN_TAGIHAN")
    private String bulanTagihan;

    @Column(name = "BILLING_CYCLE")
    private String billingCycle;

    @Column(name = "BILL_REFERENCE")
    private Integer billReference;

    @Column(name = "STATUS_PROSES")
    private String statusProses;

    @Column(name = "TANGGAL_PROSES")
    private Date tanggalProses;

    @Column(name = "NILAI_PEMBAYARAN")
    private Integer nilaiPembayaran;

    @Column(name = "NOMOR_RESI")
    private String nomorResi;

    @Column(name = "STAN")
    private String stan;

    @Column(name = "TRANS_DATE")
    private Date transDate;

    @Column(name = "CA_CODE")
    private String caCode;

    @Column(name = "TERMINAL_ID")
    private Integer terminalId;

    @Column(name = "STAND_AWAL")
    private Double standAwal;

    @Column(name = "STAND_AKHIR")
    private Double standAkhir;

    @Column(name = "GOP_MASTER_ID")
    private Integer gopMasterId;

    @Column(name = "KODE_BAYAR")
    private String kodeBayar;

    @Column(name = "METODE_PERHIT_VOL_GAS")
    private String metodePerhitVolGas;

    @Column(name = "TAGIHAN_GAS")
    private Double tagihanGas;

    @Column(name = "TAGIHAN_LAIN")
    private Double tagihanLain;

    @Column(name = "JAMINAN_PEMBAYARAN")
    private Double jaminanPembayaran;

    @Column(name = "KODE_KELOMPOK_PELANGGAN")
    private String kodeKelompokPelanggan;

    @Column(name = "TOTAL_TAGIHAN")
    private Double totalTagihan;
}
