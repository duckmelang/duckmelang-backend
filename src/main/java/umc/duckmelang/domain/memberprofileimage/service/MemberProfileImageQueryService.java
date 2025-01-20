package umc.duckmelang.domain.memberprofileimage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import java.util.List;
public interface MemberProfileImageQueryService {
    Page<String> getMemberImages(Long id, int page);
}
