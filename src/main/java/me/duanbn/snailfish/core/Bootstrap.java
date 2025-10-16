package me.duanbn.snailfish.core;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.domain.DomainObjectResgister;
import me.duanbn.snailfish.core.orm.Persistence;
import me.duanbn.snailfish.util.collection.Lists;

/**
 * @author zhilin
 * @author bingnan.dbn
 */
@Slf4j
public class Bootstrap implements InitializingBean {

    @Setter
    @Getter
    private List<String> packages;
    @Setter
    @Getter
    private boolean enableLog;
    @Setter
    @Getter
    private boolean enableDDL;
    @Setter
    @Getter
    private boolean enableSQLLog;

    @Autowired
    private RegisterFactory registerFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        // logger
        if (this.isEnableLog())
            log.info(String.format("snailfish bootstrap is starting..."));

        long start = System.currentTimeMillis();

        // scan object and register them
        List<Class<?>> classes = scanPreConfiguredPackages();
        registerBeans(classes);

        // show DDL
        if (this.isEnableDDL() && this.isEnableLog()) {
            DomainObjectResgister domainRegister = this.registerFactory.getDomainRegister();
            String sqlStatement = Persistence.getSqlStatement(domainRegister.getEntityClasses(),
                    domainRegister.getValueObjectClasses());
            System.out.println(sqlStatement);
        }

        if (this.isEnableLog())
            log.info(String.format("snailfish bootstrap start has finished in %sms",
                    (System.currentTimeMillis() - start)));
    }

    private List<Class<?>> scanPreConfiguredPackages() {
        if (CollectionUtils.isEmpty(packages)) {
            return Lists.newArrayList();
        }
        List<Class<?>> classes = Lists.newArrayList();
        for (String it : packages) {
            classes.addAll(ClassScanner.doScan(it));
        }
        return classes;
    }

    private void registerBeans(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            try {
                List<RegisterI> registers = registerFactory.getRegister(clazz);
                for (RegisterI register : registers) {
                    register.doRegistration(clazz);
                }
            } catch (Exception e) {
                throw new RegisterException("register class=" + clazz + " error", e);
            }
        }
    }

}
