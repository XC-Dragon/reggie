package com.hbr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hbr.reggie.common.BaseContext;
import com.hbr.reggie.common.R;
import com.hbr.reggie.pojo.AddressBook;
import com.hbr.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName AddressBookController
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 15:35
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getUserId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper.eq(AddressBook::getUserId, BaseContext.getUserId());
        addressBookLambdaUpdateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(addressBookLambdaUpdateWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable("id") Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            return R.error("地址不存在");
        }
        return R.success(addressBook);
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getUserId());
        addressBookLambdaQueryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);
        if (addressBook == null) {
            return R.error("地址不存在");
        }
        return R.success(addressBook);
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getUserId());
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getUserId());
        addressBookLambdaQueryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> addressBookList = addressBookService.list(addressBookLambdaQueryWrapper);
        return R.success(addressBookList);
    }

    @DeleteMapping
    public R<String> delete(String ids) {
        addressBookService.removeById(ids);
        return R.success("删除成功");
    }

}
