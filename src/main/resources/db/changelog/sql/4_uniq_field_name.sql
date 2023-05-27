-- aki_user
ALTER TABLE aki_user
    RENAME email TO user_email;
ALTER TABLE aki_user
    RENAME type TO user_type;
ALTER TABLE aki_user
    RENAME phone TO user_phone;
ALTER TABLE aki_user
    RENAME ban_reason TO user_ban_reason;
ALTER TABLE aki_user
    RENAME admin_id TO user_admin_id;

-- place_review
ALTER TABLE place_review
    RENAME status TO place_review_status;
ALTER TABLE place_review
    RENAME ban_reason TO place_review_ban_reason;
ALTER TABLE place_review
    RENAME admin_id TO place_review_admin_id;

-- rent_slot
ALTER TABLE rent_slot
    RENAME status TO rent_slot_status;

-- rent
ALTER TABLE rent
    RENAME status TO rent_status;
ALTER TABLE rent
    RENAME ban_reason TO rent_ban_reason;
ALTER TABLE rent
    RENAME admin_id TO rent_admin_id;

-- area
ALTER TABLE area
    RENAME name TO area_name;
ALTER TABLE area
    RENAME description TO area_description;
ALTER TABLE area
    RENAME address TO area_address;
ALTER TABLE area
    RENAME website TO area_website;
ALTER TABLE area
    RENAME email TO area_email;
ALTER TABLE area
    RENAME phone TO area_phone;
ALTER TABLE area
    RENAME coordinates_id TO area_coordinates_id;
ALTER TABLE area
    RENAME status TO area_status;
ALTER TABLE area
    RENAME ban_reason TO area_ban_reason;
ALTER TABLE area
    RENAME admin_id TO area_admin_id;

-- place
ALTER TABLE place
    RENAME type TO place_type;
ALTER TABLE place
    RENAME name TO place_name;
ALTER TABLE place
    RENAME description TO place_description;
ALTER TABLE place
    RENAME address TO place_address;
ALTER TABLE place
    RENAME website TO place_website;
ALTER TABLE place
    RENAME email TO place_email;
ALTER TABLE place
    RENAME phone TO place_phone;
ALTER TABLE place
    RENAME coordinates_id TO place_coordinates_id;
ALTER TABLE place
    RENAME status TO place_status;
ALTER TABLE place
    RENAME ban_reason TO place_ban_reason;
ALTER TABLE place
    RENAME admin_id TO place_admin_id;
