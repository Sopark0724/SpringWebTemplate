package com.web.template.org.domain;

import com.web.template.user.domain.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DeptMember> members = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Department parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Department> children = new ArrayList<>();

    public Department(String name, Department parent) {
        this.name = name;
        this.setParent(parent);
    }

    private void setParent(Department department) {
        this.parent = department;
        if (this.parent != null) {
            this.parent.addChildrend(this);
        }
    }

    private void addChildrend(Department department) {
        this.children.add(department);
    }

    public void addMember(DeptMember deptMember){
        members.add(deptMember);
    }

    public Long getParentId() {
        if(parent == null){
            return null;
        }

        return parent.getId();
    }
}
