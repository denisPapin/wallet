package com.myprojects.walletdemoproject.project.repository;

import com.myprojects.walletdemoproject.project.model.User;
import com.myprojects.walletdemoproject.project.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}
