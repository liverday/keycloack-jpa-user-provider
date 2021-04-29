package com.liverday.keycloak.providers.userStorage;

import com.liverday.keycloak.model.User;
import lombok.extern.jbosslog.JBossLog;
import org.apache.commons.lang.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import com.liverday.keycloak.dao.implementations.HibernateUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;

import static com.liverday.keycloak.config.FactoryConfiguration.DB_HOST_KEY;
import static com.liverday.keycloak.config.FactoryConfiguration.DB_PORT_KEY;
import static com.liverday.keycloak.config.FactoryConfiguration.DB_DATABASE_KEY;
import static com.liverday.keycloak.config.FactoryConfiguration.DB_USERNAME_KEY;
import static com.liverday.keycloak.config.FactoryConfiguration.DB_PASSWORD_KEY;
import static com.liverday.keycloak.config.FactoryConfiguration.DB_CONNECTION_NAME_KEY;

@JBossLog
public class CustomUserStorageProviderFactory implements UserStorageProviderFactory<HibernateUserStorageProvider> {
    Map<String, Object> properties;
    Map<String, EntityManagerFactory> entityManagerFactories = new HashMap<>();
    Logger logger = LoggerFactory.getLogger(CustomUserStorageProviderFactory.class);

    protected final List<ProviderConfigProperty> configMetadata;

    public CustomUserStorageProviderFactory() {
        configMetadata = ProviderConfigurationBuilder.create()
                .property().name(DB_CONNECTION_NAME_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Nome da conexão")
                .defaultValue("")
                .helpText("Nome da conexão, pode ser escolhido individualmente. Permite compartilhar conexões entre provedores com o mesmo nome. Sobrepõe propriedades salvas")
                .add()

                .property().name(DB_HOST_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Host do Banco de Dados")
                .defaultValue("localhost")
                .helpText("Host da conexão com o banco de dados")
                .add()

                .property().name(DB_PORT_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Porta do Banco de Dados")
                .defaultValue("5432")
                .add()

                .property().name(DB_DATABASE_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Nome do banco de Dados")
                .add()

                .property().name(DB_USERNAME_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Usuário do banco de dados")
                .defaultValue("user")
                .add()

                .property().name(DB_PASSWORD_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("Senha do banco de dados")
                .defaultValue("password")
                .add()

                .build();
    }

    @Override
    public String getId() {
        return "postgres-provider";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    @Override
    public HibernateUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        properties = new HashMap<>();

        MultivaluedHashMap<String, String> config = model.getConfig();
        String dbConnectionName = config.getFirst(DB_CONNECTION_NAME_KEY);
        EntityManagerFactory entityManagerFactory = entityManagerFactories.get(dbConnectionName);

        if (entityManagerFactory == null) {
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            properties.put("hibernate.connection.url",
                    String.format("jdbc:postgresql://%s:%s/%s",
                            config.getFirst(DB_HOST_KEY),
                            config.getFirst(DB_PORT_KEY),
                            config.getFirst(DB_DATABASE_KEY)));
            properties.put("hibernate.connection.username", config.getFirst(DB_USERNAME_KEY));
            properties.put("hibernate.connection.password", config.getFirst(DB_PASSWORD_KEY));
            properties.put("hibernate.show-sql", "true");
            properties.put("hibernate.archive.autodetection", "class, hbm");
            properties.put("hibernate.connection.autocommit", "true");

            logger.info("properties: {}", properties);

            entityManagerFactory = new HibernatePersistenceProvider().createContainerEntityManagerFactory(getPersistenceUnitInfo("h2userstorage"), properties);
            entityManagerFactories.put(dbConnectionName, entityManagerFactory);
        }

        return new HibernateUserStorageProvider(session, model, new HibernateUserDAO(entityManagerFactory.createEntityManager()));
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
        MultivaluedHashMap<String, String> configMap = config.getConfig();
        
        if (StringUtils.isBlank(configMap.getFirst(DB_CONNECTION_NAME_KEY)))
            throw new ComponentValidationException("Nome da conexão está vazio");

        if (StringUtils.isBlank(configMap.getFirst(DB_HOST_KEY)))
            throw new ComponentValidationException("Host do Banco de Dados está vazio");

        if (StringUtils.isBlank(configMap.getFirst(DB_PORT_KEY)))
            throw new ComponentValidationException("Porta do Banco de Dados está vazia");

        if (StringUtils.isBlank(configMap.getFirst(DB_DATABASE_KEY)))
            throw new ComponentValidationException("Nome do Banco de Dados está vazio");

        if (StringUtils.isBlank(configMap.getFirst(DB_USERNAME_KEY)))
            throw new ComponentValidationException("Usuário do Banco de Dados está vazio");

        if (StringUtils.isBlank(configMap.getFirst(DB_PASSWORD_KEY)))
            throw new ComponentValidationException("Senha do Banco de Dados está vazia");
    }

    private PersistenceUnitInfo getPersistenceUnitInfo(String name) {
        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return name;
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "org.hibernate.jpa.HibernatePersistenceProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return null;
            }

            @Override
            public List<String> getMappingFileNames() {
                return Collections.emptyList();
            }

            @Override
            public List<URL> getJarFileUrls() {
                try {
                    return Collections.list(this.getClass()
                            .getClassLoader()
                            .getResources(""));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return null;
            }

            @Override
            public List<String> getManagedClassNames() {
                List<String> managedClasses = new LinkedList<>();
                managedClasses.add(User.class.getName());
                return managedClasses;
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return SharedCacheMode.UNSPECIFIED;
            }

            @Override
            public ValidationMode getValidationMode() {
                return ValidationMode.AUTO;
            }

            @Override
            public Properties getProperties() {
                return new Properties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return "2.1";
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {
            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };
    }
}
