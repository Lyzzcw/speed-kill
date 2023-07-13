package lyzzcw.work.speedkill.goods.domain.convert;

import lyzzcw.work.speedkill.goods.domain.dto.GoodsDTO;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import lyzzcw.work.speedkill.goods.domain.vo.GoodsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/1/26
 * Time: 14:30
 * Description: No Description
 */
// spring方式加载,生成的实现类上面会自动添加一个@Component注解，可以通过Spring的 @Autowired方式进行注入
   /* 我们在编译时会报 java: No property named "numberOfSeats" exists in source parameter(s). Did you mean "null"? 错误
            经过查阅资料发现 mapstruct-processor 和 Lombok 的版本需要统一一下：
            mapstruct-processor：1.2.0.Final ， Lombok：1.16.14*/
//@Mapper(componentModel = "spring")
@Mapper
public interface GoodsConverter {
    // default方式加载
    GoodsConverter INSTANCE = Mappers.getMapper(GoodsConverter.class);


    Goods goodsDTO2Goods(GoodsDTO goodsDTO);

    GoodsVO goods2GoodsVO(Goods goods);
    List<GoodsVO> goods2GoodsVOList(List<Goods> goodsList);
}
