package com.vvv.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.vvv.blog.entity.Category;
import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.mapper.ArticleCategoryRefMapper;
import com.vvv.blog.mapper.CategoryMapper;
import com.vvv.blog.service.CategoryService;
import com.vvv.blog.util.BlogException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Objects;


/**
 * 用户管理
 *
 * @author 言曌
 * @date 2017/8/24
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleCategoryRefMapper articleCategoryRefMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Integer id) {
            List<Category> childCategory = categoryMapper.findChildCategory(id);
            if(CollUtil.isNotEmpty(childCategory)){
                throw  new BlogException(CodeEnum.NOT_ALLOW_ERR,"当前分类有子分类不能删除");
            }
            categoryMapper.deleteCategory(id);
            articleCategoryRefMapper.deleteByCategoryId(id);

    }

    @Override
    public Category getCategoryById(Integer id) {
        Category category = null;
        try {
            category = categoryMapper.getCategoryById(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据分类ID获得分类, id:{}, cause:{}", id, e);
        }
        return category;
    }

    @Override
    public void updateCategory(Category category) {
        String categoryName = category.getCategoryName();
        Category categoryDB = getCategoryByName(categoryName);
        if(Objects.nonNull(categoryDB)&&!categoryDB.getCategoryId().equals(category.getCategoryId())){
            throw new BlogException(CodeEnum.PARAM_ERR,"分类名称不能重复");
        }

        categoryMapper.update(category);

    }

    @Override
    public Category insertCategory(Category category) {
        String categoryName = category.getCategoryName();
        Category categoryDB = getCategoryByName(categoryName);
        if(Objects.nonNull(categoryDB)){
            throw new BlogException(CodeEnum.PARAM_ERR,"分类名称不能重复");
        }

        categoryMapper.insert(category);
        return category;
    }


    @Override
    public Integer countCategory() {
        Integer count = 0;
        try {
            count = categoryMapper.countCategory();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("统计分类失败, cause:{}", e);
        }
        return count;
    }


    @Override
    public List<Category> listCategory() {
        List<Category> categoryList = null;
        try {
            categoryList = categoryMapper.listCategory();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据文章获得分类列表失败, cause:{}", e);
        }
        return categoryList;
    }

    @Override
    public List<Category> listCategoryWithCount() {
        List<Category> categoryList = null;
        try {
            categoryList = categoryMapper.listCategory();
            for (int i = 0; i < categoryList.size(); i++) {
                Integer count = articleCategoryRefMapper.countArticleByCategoryId(categoryList.get(i).getCategoryId());
                categoryList.get(i).setArticleCount(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据文章获得分类列表失败, cause:{}", e);
        }
        return categoryList;
    }

    @Override
    public Category getCategoryByName(String name) {
        Category category = null;
        try {
            category = categoryMapper.getCategoryByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新分类失败, category:{}, cause:{}", category, e);
        }
        return category;
    }


}
