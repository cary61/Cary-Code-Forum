package top.cary.cary_code.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.cary.cary_code.mapper.UserMapper;
import top.cary.cary_code.utils.encryption.Hasher;

@Component
public class Initiater {

    @Autowired
    private Hasher hasher;

    @Autowired
    private UserMapper userMapper;

    @Value("${authority.root.name}")
    private String rootName;

    @Value("${authority.root.password}")
    private String rootPassword;

    public static void main(String[] args) {
        new Initiater().init();
    }

    private void init() {
        createRootUser();
    }

    private void createRootUser() {
//        userMapper.add(User.builder()
//                        .name(rootName)
//                        .passwordHash(hasher.hash(rootPassword))
//                        .authority(AuthorityEnum.ROOT)
//                .build());
    }
}
