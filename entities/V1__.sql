ALTER TABLE favoritos
    DROP CONSTRAINT favoritos_imovel_id_fkey;

ALTER TABLE favoritos
    DROP CONSTRAINT favoritos_visitante_id_fkey;

ALTER TABLE foto_vistoria_url_foto_vistoria
    DROP CONSTRAINT fks5c63v9e4s1vppnw3k3cwxwcm;

ALTER TABLE usuario
    ADD email VARCHAR(255);

ALTER TABLE usuario
    ALTER COLUMN email SET NOT NULL;

ALTER TABLE usuario
    ADD CONSTRAINT uc_usuario_email UNIQUE (email);

ALTER TABLE usuario_roles
    ADD CONSTRAINT fk_usurol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

DROP TABLE favoritos CASCADE;

DROP TABLE foto_vistoria_url_foto_vistoria CASCADE;

