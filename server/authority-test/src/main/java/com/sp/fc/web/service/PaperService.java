package com.sp.fc.web.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PaperService implements InitializingBean {

    private final HashMap<Long, Paper> paperDB = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void setPaper(Paper paper) {
        paperDB.put(paper.getPaperId(), paper);
    }

//    @PostFilter("notPrepareState(filterObject)")
    public List<Paper> getMyPapers(String username) {
//        return paperDB.values().stream().collect(Collectors.toList()); // return new ArrayList<>(paperDB.values());

        return paperDB.values().stream().filter(
                paper -> paper.getStudentIds().contains(username)
        ).collect(Collectors.toList());
    }

//    @PostAuthorize("returnObject.studentIds.contains(principal.username)")
    public Paper getPaper(long paperId) {
        return paperDB.get(paperId);
    }

    public List<Paper> getAllPapers() {
        return paperDB.values().stream().collect(Collectors.toList());
    }
}
