package com.turkcell.accountService.business.dtos.response.created;

import com.turkcell.accountService.entities.concretes.AccountTypes;
import com.turkcell.accountService.entities.enums.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatedAccountResponse {
    private String accountName;
    private String accountNumber;
    private Boolean status;
    private Action action;
    private Set<AccountTypes> accountTypes;
    private int customerId;
}