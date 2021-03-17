package com.stonezarcon.myrest;

import java.util.List;
import java.util.Set;

public interface UserRepositoryCustom {
    List<User> findUserByLastName(Set<String> lastName);
}
