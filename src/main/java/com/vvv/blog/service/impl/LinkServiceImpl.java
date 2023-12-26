package com.vvv.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vvv.blog.enums.CodeEnum;
import com.vvv.blog.service.LinkService;
import com.vvv.blog.entity.Link;
import com.vvv.blog.mapper.LinkMapper;
import com.vvv.blog.util.BlogException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;

/**
 *
 * @author 言曌
 * @date 2017/9/4
 */
@Service
public class LinkServiceImpl implements LinkService {
	
	@Autowired
	private LinkMapper linkMapper;
	
	@Override
	public Integer countLink(Integer status)  {
		return linkMapper.countLink(status);
	}
	
	@Override
	public List<Link> listLink(Integer status)  {
		return linkMapper.listLink(status);
	}

	@Override
	public void insertLink(Link link)  {
		Link repairLinkName = repairLinkName(link.getLinkName());
		if(Objects.nonNull(repairLinkName)){
			throw new BlogException(CodeEnum.PARAM_ERR,"名称不能重复");
		}
		linkMapper.insert(link);
	}

	public Link  repairLinkName(String linkName){
		Link link = linkMapper.selectOne(new QueryWrapper<Link>().lambda().eq(Link::getLinkName, linkName));
		return link;
	}
	@Override
	public void deleteLink(Integer id)  {
		linkMapper.deleteById(id);
	}

	@Override
	public void updateLink(Link link)  {
		Link repairLinkName = repairLinkName(link.getLinkName());
		if(Objects.nonNull(repairLinkName)&&!repairLinkName.getLinkId().equals(link.getLinkId())){
			throw new BlogException(CodeEnum.PARAM_ERR,"名称不能重复");
		}
		linkMapper.update(link);
	}

	@Override
	public Link getLinkById(Integer id)  {
		return linkMapper.getLinkById(id);
	}

}
