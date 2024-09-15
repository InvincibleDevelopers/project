package invincibleDevs.bookpago.Users.dto.request;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
    @ApiModelProperty(value = "카카오 회원 고유 ID", required = true)
    @NotBlank
    Long username,

    @ApiModelProperty(value = "프로필 닉네임", required = true)
    @NotBlank
    String nickname
){}
