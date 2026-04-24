package test.org.springdoc.api.v30.app12;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "hateoas-controller", description = "Hateoas Controller")
public class HateoasController {

    record MyDto(String name, int count) {
    }

    @GetMapping("/items")
    public PagedModel<EntityModel<MyDto>> getItems(PagedResourcesAssembler<MyDto> assembler, Pageable pageable) {
        return null;
    }
}
