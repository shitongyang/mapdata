package iscas.stategrid.mapdata.Service;

import iscas.stategrid.mapdata.domain.st_locationEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface st_locationService {
   List<st_locationEntity> getAll();

}
