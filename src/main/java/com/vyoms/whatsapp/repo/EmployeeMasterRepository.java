package com.vyoms.whatsapp.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vyoms.whatsapp.model.EmpMaster;

@Repository
public interface EmployeeMasterRepository extends CrudRepository<EmpMaster, Integer> {

}
