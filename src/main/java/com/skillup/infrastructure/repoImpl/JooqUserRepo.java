package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.user.UserDomain;
import com.skillup.domain.user.UserRepository;
import com.skillup.infrastructure.jooq.tables.User;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository// create a Spring object to containers
public class JooqUserRepo implements UserRepository {

    @Autowired
    DSLContext dslContext;

    public static final User USER_T = new User();
    // public static final User USER_T = new User();

    @Override
    public void createUser(UserDomain userDomain) {
        try {
            dslContext.executeInsert(toRecord(userDomain));
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public UserDomain readUserById(String id) {
        Optional<UserDomain> userRecordOptional = dslContext.selectFrom(USER_T).where(USER_T.USER_ID.eq(id))
                .fetchOptional(this:: toDomain);
        return userRecordOptional.orElse(null); //if present, .get(), else null
    }

    @Override
    public UserDomain readUserByName(String name) {
        Optional<UserDomain> userRecordOptional = dslContext.selectFrom(USER_T).where(USER_T.USER_NAME.eq(name))
                .fetchOptional(this:: toDomain);
        return userRecordOptional.orElse(null); //if present, .get(), else null
    }

    @Override
    public void updateUser(UserDomain userDomain) {

    }

    private UserRecord toRecord(UserDomain userDomain){
        UserRecord userRecord = new UserRecord();

        userRecord.setUserId(userDomain.getUserId());
        userRecord.setUserName(userDomain.getUserName());
        userRecord.setPassword(userDomain.getPassword());

        return userRecord;
    }

    public UserDomain toDomain(UserRecord userRecord){
        return UserDomain.builder().userId(userRecord.getUserId())
                .userName(userRecord.getUserName())
                .password(userRecord.getPassword()).build();
    }
}
