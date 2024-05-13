package kg.news.mapper;

import com.github.pagehelper.Page;
import kg.news.dto.UserQueryDTO;
import kg.news.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;


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
