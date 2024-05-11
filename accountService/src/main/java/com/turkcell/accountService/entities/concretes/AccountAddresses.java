package com.turkcell.accountService.entities.concretes;

import com.turkcell.accountService.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "account_addresses")
public class AccountAddresses extends BaseEntity<Integer> {

    @Column(name = "address_id")
    private int addressId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}