package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@RequestMapping("spec")
public interface GroupApi {
    /**
     * 根据cid查询对应的组以及组内的信息
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public List<SpecGroup> findGroupAndParamById(@PathVariable(value = "cid") Long cid);
}
