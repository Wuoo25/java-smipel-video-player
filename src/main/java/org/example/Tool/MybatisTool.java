package org.example.Tool;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.InputStream;

/**
 * Mybatis配置流程
 */
public class MybatisTool {
    public static <T> T getMapper(Class<T> clazz) {
        try {
            //1.读取配置文件
            InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
            //2.创建 SqlSessionFactory 的构建者对象
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            //3.使用构建者创建工厂对象 SqlSessionFactory
            SqlSessionFactory factory = builder.build(in);
            //4.使用 SqlSessionFactory 生产 SqlSession 对象
            SqlSession session = factory.openSession(true);
            return session.getMapper(clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
