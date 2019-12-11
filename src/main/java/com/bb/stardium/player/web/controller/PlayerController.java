package com.bb.stardium.player.web.controller;

import com.bb.stardium.mediafile.config.MediaFileResourceLocation;
import com.bb.stardium.mediafile.service.MediaFileService;
import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@RequestMapping("/player")
@AllArgsConstructor
public class PlayerController {
    private static final Logger log = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerService playerService;
    private final MediaFileService mediaFileService;
    private final MediaFileResourceLocation mediaFileResourceLocation;

    @GetMapping("/new")
    public String signupPage() {
        return "signup.html";
    }

    @PostMapping("/new")
    public String register(final PlayerRequestDto requestDto, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String fileName = mediaFileService.save(mediaFileResourceLocation.getLocation(), file);
            requestDto.setMediaFile(fileName);
        }
        playerService.register(requestDto);
        return "redirect:/login";
    }

    @GetMapping("/edit")
    public String editPage(final HttpSession session, Model model) {
        if (Objects.isNull(session.getAttribute("login"))) {
            return "redirect:/login";
        }
        model.addAttribute("model", session.getAttribute("login"));
        return "user_edit.html";
    }

    @PostMapping("/edit")
    public String edit(final PlayerRequestDto requestDto, final HttpSession session,
                       @RequestParam("profile") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (Objects.isNull(session.getAttribute("login"))) {
            return "redirect:/login";
        }


        if (file != null && !file.isEmpty()) {
            log.warn(file.getOriginalFilename());
            String fileName = mediaFileService.save(mediaFileResourceLocation.getLocation(), file);
            requestDto.setMediaFile(fileName);
        }

        final PlayerResponseDto sessionDto = (PlayerResponseDto) session.getAttribute("login");
        final PlayerResponseDto responseDto = playerService.update(requestDto, sessionDto);
        session.setAttribute("login", responseDto);
        redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
        return "redirect:/";
    }
}
