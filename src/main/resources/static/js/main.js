(function ($) {
    'use strict';

    // Dropdown on mouse hover
    $(document).ready(function () {
        function toggleNavbarMethod() {
            if ($(window).width() > 992) {
                $('.navbar .dropdown')
                .on('mouseover', function () {
                    $('.dropdown-toggle', this).trigger('click');
                })
                .on('mouseout', function () {
                    $('.dropdown-toggle', this).trigger('click').blur();
                });
            } else {
                $('.navbar .dropdown').off('mouseover').off('mouseout');
            }
        }
        toggleNavbarMethod();
        $(window).resize(toggleNavbarMethod);
    });

    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({ scrollTop: 0 }, 1500, 'easeInOutExpo');
        return false;
    });

    // Vendor carousel
    $('.vendor-carousel').owlCarousel({
        loop: true,
        margin: 29,
        nav: false,
        autoplay: true,
        smartSpeed: 1000,
        responsive: {
            0: {
                items: 2,
            },
            576: {
                items: 3,
            },
            768: {
                items: 4,
            },
            992: {
                items: 5,
            },
            1200: {
                items: 6,
            },
        },
    });

    // Related carousel
    $('.related-carousel').owlCarousel({
        loop: true,
        margin: 29,
        nav: false,
        autoplay: true,
        smartSpeed: 1000,
        responsive: {
            0: {
                items: 1,
            },
            576: {
                items: 2,
            },
            768: {
                items: 3,
            },
            992: {
                items: 4,
            },
        },
    });

    // Product Quantity
    $('.quantity button').on('click', function () {
        var button = $(this);
        var oldValue = button.parent().parent().find('input').val();
        if (button.hasClass('btn-plus')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
        button.parent().parent().find('input').val(newVal);
    });
})(jQuery);

// pwd, pwd2 일치여부 확인
const pwdChk = document.getElementById('pwd2');

pwdChk.addEventListener('keyup', checkPw);

function checkPw() {
    let password = document.getElementById('pwd').value;
    let password2 = document.getElementById('pwd2').value;
    let chk = document.getElementById('chk');

    if (password === password2) {
        chk.innerHTML =
            '<i class="fa-regular fa-circle-check"></i>' +
            '&nbsp; 비밀번호가 일치합니다.';
        chk.style.color = 'green';
    }

    if (password != password2) {
        chk.innerHTML =
            '<i class="fa-solid fa-triangle-exclamation"></i>' +
            '&nbsp; 비밀번호가 일치하지 않습니다.';
        chk.style.color = 'red';
    }
}

// 휴대폰 번호 유효성 체크, 인증번호 메시지 전송
const authNumReq = document.getElementById('authNumReq');
const authNumRes = document.getElementById('authNumRes');
const phoneNum = document.getElementById('phone');

authNumReq.addEventListener('click', sendNumber);

function sendNumber() {
    let result = /^(01[016789]{1})-?[0-9]{3,4}-?[0-9]{4}$/;

    if (result.test(phoneNum.value)) {
        // Swal.fire('입력하신 휴대폰 번호로 \n 인증번호 6자리가 전송되었습니다.');

        Swal.fire({
            icon: 'success',
            html: '입력하신 휴대폰 번호로 <br> 인증번호 6자리가 전송되었습니다.',
            confirmButtonColor: '#1f9bcf',
        });

        authNumRes.removeAttribute('disabled');
        authNumReq.setAttribute('disabled', true);
        phoneNum.setAttribute('readonly', true);
    } else {
        // Swal.fire('유효하지 않은 휴대폰 번호입니다.');

        Swal.fire({
            icon: 'warning',
            text: '유효하지 않은 휴대폰 번호입니다.',
            confirmButtonColor: '#1f9bcf',
        });
    }
}

// 회원가입 유효성 검사

let availableEmail = false;
let availableNickname = false;
let availablePhoneNumber = false;

const btnCheckEmail = document.getElementById('btnCheckEmail');
const btnCheckNickname = document.getElementById('btnCheckNickname');

btnCheckEmail.addEventListener('click', isEmailAvailable);
btnCheckNickname.addEventListener('click', isNicknameAvailable);

function isEmailAvailable() {
    let email = document.getElementById('email').value;
    let regExp =
        /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

    if (!regExp.test(email)) {
        Swal.fire({
            icon: 'warning',
            text: '이메일 형식이 올바르지 않습니다.',
            confirmButtonColor: '#1f9bcf',
        });
        return;
    }

    btnCheckEmail.setAttribute('disabled', true);
}

function isNicknameAvailable() {
    var nickname = document.getElementById('nickname').value;
    if (!nickname) {
        Swal.fire({
            icon: 'warning',
            text: '닉네임을 입력해주세요.',
            confirmButtonColor: '#1f9bcf',
        });
        return;
    }

    btnCheckNickname.setAttribute('disabled', true);
}
