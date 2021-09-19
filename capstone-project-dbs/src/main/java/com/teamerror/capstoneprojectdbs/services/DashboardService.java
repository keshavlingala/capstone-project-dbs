package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.models.ClientWiseStats;
import com.teamerror.capstoneprojectdbs.models.CustodianWiseStats;
import com.teamerror.capstoneprojectdbs.repositories.OrderBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DashboardService {

    OrderBookRepository orderBookRepository;

    public List<CustodianWiseStats> getCustodianWiseStats() {
        return orderBookRepository.getCustodianWiseStats();
    }

    public List<ClientWiseStats> getClientWiseStats() {
        return orderBookRepository.getClientWiseStats();
    }


}
