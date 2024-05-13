package kg.news.mapper;

import kg.news.dto.UserQueryDTO;
import kg.news.entity.User;
import kg.news.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;

@Mapper
public interface UserMapper {
    /**
     * 获取用户列表
     *
     * @param userQueryDTO 用户查询条件
     * @return 用户列表
     */
    Page<UserVO> queryUsers(UserQueryDTO userQueryDTO);
}
