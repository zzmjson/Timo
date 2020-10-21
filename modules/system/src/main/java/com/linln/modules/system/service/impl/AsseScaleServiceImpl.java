package com.linln.modules.system.service.impl;

import com.linln.modules.system.domain.AsseScale;
import com.linln.modules.system.domain.Scale;
import com.linln.modules.system.repository.AsseScaleRepository;
import com.linln.modules.system.service.AsseScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsseScaleServiceImpl implements AsseScaleService {
    @Autowired
    private AsseScaleRepository asseScaleRepository;
    @Override
    public AsseScale save(AsseScale scale) {
        AsseScale save = asseScaleRepository.save(scale);
        return save;
    }
}
