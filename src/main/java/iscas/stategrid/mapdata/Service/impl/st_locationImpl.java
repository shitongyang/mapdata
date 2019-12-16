package iscas.stategrid.mapdata.Service.impl;

import iscas.stategrid.mapdata.Service.st_locationService;
import iscas.stategrid.mapdata.domain.st_locationEntity;
import iscas.stategrid.mapdata.mapper.st_locationEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class st_locationImpl implements st_locationService {
    @Autowired
    private st_locationEntityMapper locationEntitymapper;
    @Override
    public List<st_locationEntity> getAll() {
        List<st_locationEntity> list= locationEntitymapper.selectAll();
        return list;
    }

}
